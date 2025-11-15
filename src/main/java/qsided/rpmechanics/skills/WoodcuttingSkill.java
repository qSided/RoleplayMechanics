package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.config.experience_values.BlockExperience;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static qsided.rpmechanics.RoleplayMechanicsCommon.getWoodcuttingXpValues;
import static qsided.rpmechanics.StateManager.getPlayerState;

public final class WoodcuttingSkill {

    // Prevent nuking huge forests in one go
    private static final int MAX_TREE_BLOCKS = 64;

    // Minimum woodcutting level to unlock tree harvesting
    private static final int TREE_HARVEST_MIN_LEVEL = 5;

    private WoodcuttingSkill() {}

    public static void register() {
        // 1) Tree harvesting BEFORE vanilla actually breaks the block
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) return true;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return true;
            if (!(world instanceof ServerWorld serverWorld)) return true;

            PlayerData data = getPlayerState(serverPlayer);

            // Only logs and only if player is high enough level
            if (!isLog(state)) return true;
            int level = data.skillLevels.getOrDefault("woodcutting", 1);
            if (level < TREE_HARVEST_MIN_LEVEL) return true;

            // Harvest the rest of the tree; vanilla will still break the original block
            harvestTree(serverWorld, pos, state, serverPlayer, data);

            return true; // allow vanilla break to continue
        });

        // 2) XP + double drops AFTER block is broken
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) return;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
            if (!(world instanceof ServerWorld serverWorld)) return;

            PlayerData data = getPlayerState(serverPlayer);

            Optional<BlockExperience> maybeXp = findWoodcuttingEntry(state);
            if (maybeXp.isEmpty()) return;

            // Grant configured XP for this block
            maybeXp.get().getExperience().forEach((skill, value) ->
                    IncreaseSkillExperienceCallback.EVENT.invoker()
                            .increaseExp(serverPlayer, data, skill, value)
            );

            // Try to give extra drops according to woodcutting level
            maybeGiveExtraDrop(serverWorld, pos, state, blockEntity, serverPlayer, data);
        });
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static boolean isLog(BlockState state) {
        // You can restrict to exact block type if you prefer
        return state.isIn(BlockTags.LOGS);
    }

    private static Optional<BlockExperience> findWoodcuttingEntry(BlockState state) {
        String id = state.getBlock().asItem().toString();
        return getWoodcuttingXpValues().stream()
                .filter(bx -> bx.getId().equals(id))
                .findFirst();
    }

    private static void maybeGiveExtraDrop(ServerWorld world,
                                           BlockPos pos,
                                           BlockState state,
                                           BlockEntity be,
                                           ServerPlayerEntity player,
                                           PlayerData data) {

        int level = data.skillLevels.getOrDefault("woodcutting", 1);
        if (level <= 1) return;

        int roll = ThreadLocalRandom.current().nextInt(100) + 1;
        // same formula you had: (level - 1)% chance
        if (roll > (level - 1)) return;

        var tool = player.getMainHandStack();
        Block.getDroppedStacks(state, world, pos, be, player, tool)
                .forEach(stack -> Block.dropStack(world, pos, stack));
    }

    /**
     * Harvests connected logs around the origin.
     * Vanilla still handles breaking the original block; we break the rest here.
     */
    private static void harvestTree(ServerWorld world,
                                    BlockPos origin,
                                    BlockState originState,
                                    ServerPlayerEntity player,
                                    PlayerData data) {

        Block logBlock = originState.getBlock();

        Queue<BlockPos> open = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        open.add(origin);
        visited.add(origin);

        while (!open.isEmpty() && visited.size() < MAX_TREE_BLOCKS) {
            BlockPos current = open.poll();

            // Check a 3x3x3 cube around the current block
            for (BlockPos neighbor : BlockPos.iterate(current.add(-1, -1, -1), current.add(1, 1, 1))) {
                if (!visited.add(neighbor)) continue; // already processed
                if (visited.size() > MAX_TREE_BLOCKS) break;

                BlockState neighborState = world.getBlockState(neighbor);
                if (!isSameLogType(logBlock, neighborState)) continue;

                open.add(neighbor);

                // Skip origin: vanilla will break it and handle drops/tool damage
                if (neighbor.equals(origin)) continue;

                // Break the block as if the player mined it
                boolean dropped = world.breakBlock(neighbor, true, player);

                // If it actually broke, award woodcutting XP for this block too
                if (dropped) {
                    awardXpForState(neighborState, player, data);
                }
            }
        }
    }

    private static boolean isSameLogType(Block baseLog, BlockState candidateState) {
        // Optional: Require exact same block type:
        // return candidateState.isOf(baseLog);

        // Allow any log block (using tag) so different wood types in a tree still count:
        return candidateState.isIn(BlockTags.LOGS);
    }

    private static void awardXpForState(BlockState state,
                                        ServerPlayerEntity player,
                                        PlayerData data) {

        findWoodcuttingEntry(state).ifPresent(bx ->
                bx.getExperience().forEach((skill, value) ->
                        IncreaseSkillExperienceCallback.EVENT.invoker()
                                .increaseExp(player, data, skill, value)
                )
        );
    }
}
