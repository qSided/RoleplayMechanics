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

    private static final int VEIN_MINING_UNLOCK_LEVEL = 10;
    private static final int VEIN_MINING_BASE_BLOCKS = 3;
    private static final int VEIN_MINING_BLOCKS_PER_LEVEL = 2;

    private static final Direction[] VEIN_DIRECTIONS = Direction.values();
    private static final ThreadLocal<Boolean> VEIN_MINING_ACTIVE = ThreadLocal.withInitial(() -> false);

    public static void register() {

        //
        // 1) XP + durability AFTER break (unchanged)
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

            if ((stack.isIn(ItemTags.PICKAXES) || stack.isIn(ItemTags.SHOVELS))
                    && stack.isDamaged()
                    && randomInt <= (miningLevel - 1)) {
                stack.setDamage(Math.max(stack.getDamage() - 1, 0));
            }

            //
            // 2) Vein mining – now also triggered from AFTER
            //
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
            if (VEIN_MINING_ACTIVE.get()) return; // ignore recursive calls

            PlayerData pdata = StateManager.getPlayerState(serverPlayer);
            int level = pdata.skillLevels.getOrDefault("mining", 1);
            if (level < VEIN_MINING_UNLOCK_LEVEL) return;

            var mainHand = serverPlayer.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!mainHand.isIn(ItemTags.PICKAXES)) return;

            // Only vein mine ores
            if (!isOre(blockState)) return;

            int effectiveLevel = Math.max(0, level - VEIN_MINING_UNLOCK_LEVEL);
            int maxBlocks = VEIN_MINING_BASE_BLOCKS + (effectiveLevel * VEIN_MINING_BLOCKS_PER_LEVEL);
            if (maxBlocks <= 0) return;

            try {
                VEIN_MINING_ACTIVE.set(true);
                veinMine((ServerWorld) world, pos, blockState.getBlock(), serverPlayer, maxBlocks);
            } finally {
                VEIN_MINING_ACTIVE.set(false);
            }
        });

        //
        // 3) Tool-based mining/chopping speed attribute (unchanged)
        //
        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
            if (!(livingEntity instanceof ServerPlayerEntity player)) return;
            if (equipmentSlot != EquipmentSlot.MAINHAND) return;

            PlayerData state = StateManager.getPlayerState(player);
            Identifier efficiencyModifierId = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "efficiency_modifier");

            EntityAttributeInstance attr = player.getAttributeInstance(EntityAttributes.PLAYER_MINING_EFFICIENCY);
            if (attr == null) return;

            if (currentStack.isIn(ItemTags.PICKAXES) || currentStack.isIn(ItemTags.SHOVELS)) {
                double bonus = (state.skillLevels.getOrDefault("mining", 1) - 1)
                        * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.miningSettings.miningSpeed();

                attr.overwritePersistentModifier(new EntityAttributeModifier(
                        efficiencyModifierId, bonus, EntityAttributeModifier.Operation.ADD_VALUE
                ));
            } else if (currentStack.isIn(ItemTags.AXES)) {
                double bonus = (state.skillLevels.getOrDefault("woodcutting", 1) - 1)
                        * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.woodcuttingSettings.choppingSpeed();

                attr.overwritePersistentModifier(new EntityAttributeModifier(
                        efficiencyModifierId, bonus, EntityAttributeModifier.Operation.ADD_VALUE
                ));
            } else {
                if (attr.hasModifier(efficiencyModifierId)) {
                    attr.removeModifier(efficiencyModifierId);
                }
            }
        });
    }

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

    private static void veinMine(ServerWorld world,
                                 BlockPos origin,
                                 Block targetBlock,
                                 ServerPlayerEntity player,
                                 int maxBlocks) {

        Set<BlockPos> visited = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();

        visited.add(origin);

        // Start at neighbors so vanilla handles origin
        for (Direction dir : VEIN_DIRECTIONS) {
            queue.add(origin.offset(dir));
        }

        int broken = 0;

        while (!queue.isEmpty() && broken < maxBlocks) {
            BlockPos current = queue.poll();
            if (!visited.add(current)) continue;

            BlockState state = world.getBlockState(current);
            if (!state.isOf(targetBlock)) continue;

            boolean success = world.breakBlock(current, true, player);
            if (!success) continue;

            broken++;

            for (Direction dir : VEIN_DIRECTIONS) {
                BlockPos next = current.offset(dir);
                if (!visited.contains(next)) queue.add(next);
            }
        }
    }
}