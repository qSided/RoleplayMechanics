package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.PlayerStartBreakingEvent;

import java.util.*;

/**
 * Mining skill:
 * - Gives XP per block from config (getMiningXpValues)
 * - Small chance to repair tool durability based on mining level
 * - Adds mining/chopping speed attribute when holding a tool
 * - Vein mining: break connected ore blocks based on mining level
 */
public class MiningSkill {

    // Vein-mining tuning constants (you can later move these to a config)
    private static final int VEIN_MINING_UNLOCK_LEVEL = 5;
    private static final int VEIN_MINING_BASE_BLOCKS = 3;
    private static final int VEIN_MINING_BLOCKS_PER_LEVEL = 2;

    // Directions to search around a block for vein mining
    private static final Direction[] VEIN_DIRECTIONS = Direction.values();

    // Prevent recursive triggering of vein mining when we break blocks programmatically
    private static final ThreadLocal<Boolean> VEIN_MINING_ACTIVE = ThreadLocal.withInitial(() -> false);

    public static void register() {

        //
        // 1) XP + durability handling AFTER a block is broken
        //
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
            if (world.isClient) return;

            PlayerData data = StateManager.getPlayerState(player);
            int miningLevel = data.skillLevels.getOrDefault("mining", 1);

            // XP per block
            RoleplayMechanicsCommon.getMiningXpValues().forEach(block -> {
                if (blockState.getBlock().asItem().toString().equals(block.getId())) {
                    block.getExperience().forEach((skill, value) -> {
                        IncreaseSkillExperienceCallback.EVENT.invoker()
                                .increaseExp((ServerPlayerEntity) player, data, skill, value);
                    });
                }
            });

            // Small chance to repair tool durability based on mining level
            Random r = new Random();
            int randomInt = r.nextInt(100) + 1;

            var stack = player.getEquippedStack(EquipmentSlot.MAINHAND);

            // (pick OR shovel) AND damaged AND random <= (level - 1)
            if ((stack.isIn(ItemTags.PICKAXES) || stack.isIn(ItemTags.SHOVELS))
                    && stack.isDamaged()
                    && randomInt <= (miningLevel - 1)) {

                stack.setDamage(Math.max(stack.getDamage() - 1, 0));
            }
        });

        //
        // 2) Vein mining on block break start
        //    Triggered by AbstractBlockStateMixin -> PlayerStartBreakingEvent
        //
        PlayerStartBreakingEvent.EVENT.register((world, pos, state, player) -> {
            if (world.isClient) return ActionResult.PASS;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;

            // Don't re-trigger while we're already vein-mining
            if (VEIN_MINING_ACTIVE.get()) return ActionResult.PASS;

            PlayerData data = StateManager.getPlayerState(serverPlayer);
            int miningLevel = data.skillLevels.getOrDefault("mining", 1);

            // Unlock at a certain mining level
            if (miningLevel < VEIN_MINING_UNLOCK_LEVEL) return ActionResult.PASS;

            // Only vein-mine with pickaxes (you can extend this)
            var mainHand = serverPlayer.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!mainHand.isIn(ItemTags.PICKAXES)) return ActionResult.PASS;

            // Only vein-mine ores (using real vanilla ore tags)
            if (!isOre(state)) return ActionResult.PASS;

            // Classic behavior -> cancel vein mining while sneaking
            if (serverPlayer.isSneaking()) return ActionResult.PASS;

            int effectiveLevel = miningLevel - VEIN_MINING_UNLOCK_LEVEL;
            if (effectiveLevel < 0) effectiveLevel = 0;

            int maxBlocks = VEIN_MINING_BASE_BLOCKS + (effectiveLevel * VEIN_MINING_BLOCKS_PER_LEVEL);
            if (maxBlocks <= 0) return ActionResult.PASS;

            // Run the vein miner
            try {
                VEIN_MINING_ACTIVE.set(true);
                veinMine((ServerWorld) world, pos, state.getBlock(), serverPlayer, maxBlocks);
            } finally {
                VEIN_MINING_ACTIVE.set(false);
            }

            // We don't cancel vanilla behavior, just add extra breaks
            return ActionResult.PASS;
        });

        //
        // 3) Tool-based mining/chopping speed attribute
        //
        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
            if (!(livingEntity instanceof ServerPlayerEntity player)) return;
            if (equipmentSlot != EquipmentSlot.MAINHAND) return;

            PlayerData state = StateManager.getPlayerState(player);
            Identifier efficiencyModifierId = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "efficiency_modifier");

            EntityAttributeInstance attr = player.getAttributeInstance(EntityAttributes.PLAYER_MINING_EFFICIENCY);
            if (attr == null) return;

            // Pickaxe or shovel -> mining skill
            if (currentStack.isIn(ItemTags.PICKAXES) || currentStack.isIn(ItemTags.SHOVELS)) {
                double bonus = (state.skillLevels.getOrDefault("mining", 1) - 1)
                        * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.miningSettings.miningSpeed();

                attr.overwritePersistentModifier(new EntityAttributeModifier(
                        efficiencyModifierId,
                        bonus,
                        EntityAttributeModifier.Operation.ADD_VALUE
                ));
                RoleplayMechanicsCommon.LOGGER.info("Added mining efficiency modifier");

                // Axe -> woodcutting skill
            } else if (currentStack.isIn(ItemTags.AXES)) {
                double bonus = (state.skillLevels.getOrDefault("woodcutting", 1) - 1)
                        * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.woodcuttingSettings.choppingSpeed();

                attr.overwritePersistentModifier(new EntityAttributeModifier(
                        efficiencyModifierId,
                        bonus,
                        EntityAttributeModifier.Operation.ADD_VALUE
                ));
                RoleplayMechanicsCommon.LOGGER.info("Added chopping efficiency modifier");

            } else {
                // No relevant tool in main hand – remove modifier if present
                if (attr.hasModifier(efficiencyModifierId)) {
                    attr.removeModifier(efficiencyModifierId);
                    RoleplayMechanicsCommon.LOGGER.info("Removed efficiency modifier");
                }
            }
        });
    }

    /**
     * Checks if a block state is considered an ore by vanilla tags.
     * (You can later swap this to a custom tag like rpmechanics:vein_mineable.)
     */
    private static boolean isOre(BlockState state) {
        return state.isIn(BlockTags.GOLD_ORES)
                || state.isIn(BlockTags.IRON_ORES)
                || state.isIn(BlockTags.DIAMOND_ORES)
                || state.isIn(BlockTags.REDSTONE_ORES)
                || state.isIn(BlockTags.LAPIS_ORES)
                || state.isIn(BlockTags.COAL_ORES)
                || state.isIn(BlockTags.EMERALD_ORES)
                || state.isIn(BlockTags.COPPER_ORES);
    }

    /**
     * Performs a flood-fill vein mining of connected blocks of the same type,
     * up to maxBlocks (excluding the original block, which vanilla will break).
     */
    private static void veinMine(ServerWorld world,
                                 BlockPos origin,
                                 Block targetBlock,
                                 ServerPlayerEntity player,
                                 int maxBlocks) {

        Set<BlockPos> visited = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();

        visited.add(origin);

        // Start search from neighbors, let vanilla break the origin
        for (Direction dir : VEIN_DIRECTIONS) {
            queue.add(origin.offset(dir));
        }

        int broken = 0;

        while (!queue.isEmpty() && broken < maxBlocks) {
            BlockPos current = queue.poll();
            if (!visited.add(current)) continue;

            BlockState state = world.getBlockState(current);
            if (!state.isOf(targetBlock)) continue;

            // Break the block, dropping items as if the player mined it
            boolean success = world.breakBlock(current, true, player);
            if (!success) continue;

            broken++;

            // Queue neighbors of this block for further searching
            for (Direction dir : VEIN_DIRECTIONS) {
                BlockPos next = current.offset(dir);
                if (!visited.contains(next)) {
                    queue.add(next);
                }
            }
        }
    }
}
