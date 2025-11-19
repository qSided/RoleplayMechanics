package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.PlayerCancelBreakingCallback;
import qsided.rpmechanics.events.PlayerStartBreakingEvent;

import java.util.*;

import static qsided.rpmechanics.RoleplayMechanicsCommon.*;
import static qsided.rpmechanics.StateManager.getPlayerState;

public class Harvesting {

    // hard caps to avoid insane traversals
    private static final int MAX_HARVEST_BLOCKS = 256;
    private static final int MAX_SCAN_HEIGHT    = 36; // used in getBlockAmount, same as before

    static boolean sneakingAtStart;
    static BlockPos originBlock;

    public static boolean isSneakingAtStart() {
        return sneakingAtStart;
    }

    public static void setSneakingAtStart(boolean sneakingAtStart) {
        Harvesting.sneakingAtStart = sneakingAtStart;
    }

    public static BlockPos getOriginBlock() {
        return originBlock;
    }

    public static void setOriginBlock(BlockPos originBlock) {
        Harvesting.originBlock = originBlock;
    }

    public static void initialize() {
        Identifier modifierId = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "tree_and_ore_harvesting");

        // When player starts breaking block: apply slow-down modifier based on how many blocks we might break
        PlayerStartBreakingEvent.EVENT.register((world, pos, blockState, player) -> {
            double amountToDecrease = Math.min(0.82, getBlockAmount(world, pos) * 0.012);
            if (player instanceof ServerPlayerEntity serverPlayer) {
                PlayerData state = StateManager.getPlayerState(serverPlayer);
                boolean meetsMining = state.skillLevels.getOrDefault("mining", 1)
                        >= OWO_CONFIG.skillOptions.miningSettings.levelForVeinMining();
                boolean meetsWoodcutting = state.skillLevels.getOrDefault("woodcutting", 1)
                        >= OWO_CONFIG.skillOptions.woodcuttingSettings.levelForTreeChopping();

                if (meetsWoodcutting && getWoodcuttingXpValues().stream()
                        .anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString()))) {

                    if (serverPlayer.getMainHandStack().getItem() instanceof AxeItem && serverPlayer.isSneaking()) {
                        setSneakingAtStart(true);
                        setOriginBlock(pos);
                        var attr = serverPlayer.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
                        if (attr != null && !attr.hasModifier(modifierId)) {
                            attr.addTemporaryModifier(new EntityAttributeModifier(
                                    modifierId, -amountToDecrease, EntityAttributeModifier.Operation.ADD_VALUE
                            ));
                        }
                    }
                }

                if (meetsMining && getMiningXpValues().stream()
                        .anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString()))) {

                    if (serverPlayer.getMainHandStack().getItem() instanceof PickaxeItem && serverPlayer.isSneaking()) {
                        setSneakingAtStart(true);
                        setOriginBlock(pos);
                        var attr = serverPlayer.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
                        if (attr != null && !attr.hasModifier(modifierId)) {
                            attr.addTemporaryModifier(new EntityAttributeModifier(
                                    modifierId, -amountToDecrease, EntityAttributeModifier.Operation.ADD_VALUE
                            ));
                        }
                    }
                }
            }
            return ActionResult.PASS;
        });

        // When breaking is cancelled/aborted: remove modifier
        PlayerCancelBreakingCallback.EVENT.register(((world, pos, blockState, player) -> {
            var attr = player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
            if (attr != null && attr.hasModifier(modifierId)) {
                attr.removeModifier(modifierId);
                LOGGER.info("Removed modifier from player: {}", player.getName().getString());
            }
            return ActionResult.PASS;
        }));

        // BEFORE: only allow auto-harvest if they were sneaking at start & meet level
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, blockState, blockEntity) -> {
            if (world.isClient) return true;
            PlayerData state = StateManager.getPlayerState(player);
            boolean meetsMining = state.skillLevels.getOrDefault("mining", 1)
                    >= OWO_CONFIG.skillOptions.miningSettings.levelForVeinMining();
            boolean meetsWoodcutting = state.skillLevels.getOrDefault("woodcutting", 1)
                    >= OWO_CONFIG.skillOptions.woodcuttingSettings.levelForTreeChopping();

            if ((meetsMining || meetsWoodcutting) && player.isSneaking()) {
                return isSneakingAtStart();
            }
            return true;
        });

        // AFTER: actually break connected blocks + give XP, etc.
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
            if (world.isClient) return;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

            PlayerData state = getPlayerState(serverPlayer);

            // avoid obvious “world spam” blocks
            String blockId = blockState.getBlock().asItem().toString();
            if (blockId.contains("dirt") || blockId.contains("grass") || blockId.contains("stone")) {
                clearModifierAndReset(serverPlayer, modifierId);
                return;
            }

            int amount = getBlockAmount(world, pos);
            if (amount <= 0) {
                clearModifierAndReset(serverPlayer, modifierId);
                return;
            }

            // Iterative, bounded connected-block search
            List<BlockPos> blocksToBreak =
                    getConnectedBlocks(world, pos, new ArrayList<>(), amount, blockState.getBlock());

            // Need hunger like sprinting
            if (serverPlayer.getHungerManager().getFoodLevel() >= 6F) {

                // Tree harvesting (woodcutting)
                if (serverPlayer.isSneaking()
                        && state.skillLevels.getOrDefault("woodcutting", 1)
                        >= OWO_CONFIG.skillOptions.woodcuttingSettings.levelForTreeChopping()
                        && getWoodcuttingXpValues().stream().anyMatch(bx -> bx.getId().equals(blockId))
                        && serverPlayer.getMainHandStack().getItem() instanceof AxeItem) {

                    if (isSneakingAtStart() && pos.equals(getOriginBlock())) {
                        for (BlockPos blockPos : blocksToBreak) {
                            getMiningXpValues().forEach(blockExperience -> {
                                if (blockExperience.getId().equals(blockId)) {
                                    blockExperience.getExperience().forEach((skill, amountXp) -> {
                                        IncreaseSkillExperienceCallback.EVENT.invoker()
                                                .increaseExp(serverPlayer, state, skill, amountXp / 3);
                                    });
                                }
                            });
                            world.breakBlock(blockPos, true, serverPlayer);
                            serverPlayer.getHungerManager().addExhaustion(0.4F);
                            serverPlayer.getMainHandStack().damage(1, serverPlayer, EquipmentSlot.MAINHAND);
                        }
                    }
                }

                // Ore harvesting (mining)
                if (serverPlayer.isSneaking()
                        && state.skillLevels.getOrDefault("mining", 1)
                        >= OWO_CONFIG.skillOptions.miningSettings.levelForVeinMining()
                        && getMiningXpValues().stream().anyMatch(bx -> bx.getId().equals(blockId))
                        && serverPlayer.getMainHandStack().getItem() instanceof PickaxeItem) {

                    if (isSneakingAtStart() && pos.equals(getOriginBlock())) {
                        for (BlockPos blockPos : blocksToBreak) {
                            getMiningXpValues().forEach(blockExperience -> {
                                if (blockExperience.getId().equals(blockId)) {
                                    blockExperience.getExperience().forEach((skill, amountXp) -> {
                                        IncreaseSkillExperienceCallback.EVENT.invoker()
                                                .increaseExp(serverPlayer, state, skill, amountXp / 3);
                                    });
                                }
                            });
                            world.breakBlock(blockPos, true, serverPlayer);
                            serverPlayer.getHungerManager().addExhaustion(0.4F);
                            serverPlayer.getMainHandStack().damage(1, serverPlayer, EquipmentSlot.MAINHAND);
                        }
                    }
                }
            }

            clearModifierAndReset(serverPlayer, modifierId);
        });
    }

    private static void clearModifierAndReset(ServerPlayerEntity player, Identifier modifierId) {
        var attr = player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
        if (attr != null && attr.hasModifier(modifierId)) {
            attr.removeModifier(modifierId);
        }
        setSneakingAtStart(false);
    }

    /**
     * Counts how many candidate blocks (woodcutting or mining XP blocks)
     * are in a 5×5 column above the origin (same as your original logic, but
     * with an explicit height cap constant).
     */
    public static int getBlockAmount(World world, BlockPos pos) {

        int blockAmount = 0;

        for (int y = 1; y <= MAX_SCAN_HEIGHT; y++) {

            for (BlockPos bPos : BlockPos.iterate(
                    pos.getX() - 2, pos.getY() + (y - 1), pos.getZ() - 2,
                    pos.getX() + 2, pos.getY() + (y - 1), pos.getZ() + 2)) {

                BlockState bState = world.getBlockState(bPos);
                Block bBlock = bState.getBlock();
                String id = bBlock.asItem().toString();

                if (getWoodcuttingXpValues().stream().anyMatch(block -> block.getId().equals(id))
                        || getMiningXpValues().stream().anyMatch(block -> block.getId().equals(id))) {
                    blockAmount++;
                }
            }
        }
        return blockAmount;
    }

    /**
     * Iterative, bounded connected-block search.
     * Replaces the old recursive getConnectedBlocks to avoid StackOverflowError.
     *
     * @param world          world
     * @param pos            origin block position (block already broken; we search around it)
     * @param connectedBlocks list to fill (will be appended to)
     * @param amount         suggested max number of blocks (we clamp with MAX_HARVEST_BLOCKS)
     * @param blockToLookFor target block type
     */
    public static List<BlockPos> getConnectedBlocks(World world,
                                                    BlockPos pos,
                                                    List<BlockPos> connectedBlocks,
                                                    int amount,
                                                    Block blockToLookFor) {

        if (amount <= 0) return connectedBlocks;

        int maxBlocks = Math.min(amount, MAX_HARVEST_BLOCKS);

        // Use a set for O(1) membership checks
        Set<BlockPos> visited = new HashSet<>(connectedBlocks);
        Deque<BlockPos> queue = new ArrayDeque<>();

        // Seed: neighbors in a 3×3×3 cube around the original broken block
        outer:
        for (BlockPos neighbor : BlockPos.iterate(
                pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)) {

            if (neighbor.equals(pos)) continue; // origin is already broken
            BlockState state = world.getBlockState(neighbor);
            if (!state.getBlock().equals(blockToLookFor)) continue;

            BlockPos imm = neighbor.toImmutable();
            if (!visited.add(imm)) continue;

            connectedBlocks.add(imm);
            queue.add(imm);

            if (connectedBlocks.size() >= maxBlocks) {
                break outer;
            }
        }

        // BFS: walk outward from found neighbors, 3×3×3 each step
        while (!queue.isEmpty() && connectedBlocks.size() < maxBlocks) {
            BlockPos current = queue.poll();

            for (BlockPos neighbor : BlockPos.iterate(
                    current.getX() - 1, current.getY() - 1, current.getZ() - 1,
                    current.getX() + 1, current.getY() + 1, current.getZ() + 1)) {

                if (!visited.add(neighbor)) continue;

                BlockState state = world.getBlockState(neighbor);
                if (!state.getBlock().equals(blockToLookFor)) continue;

                BlockPos imm = neighbor.toImmutable();
                connectedBlocks.add(imm);

                if (connectedBlocks.size() >= maxBlocks) break;

                queue.add(imm);
            }
        }

        return connectedBlocks;
    }
}
