package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import qsided.rpmechanics.events.PlayerCancelBreakingEvent;
import qsided.rpmechanics.events.PlayerStartBreakingEvent;

import java.util.ArrayList;
import java.util.List;

import static qsided.rpmechanics.RoleplayMechanicsCommon.*;
import static qsided.rpmechanics.StateManager.getPlayerState;

public class Harvesting {
    
    public static boolean isSneakingAtStart() {
        return sneakingAtStart;
    }
    
    public static void setSneakingAtStart(boolean sneakingAtStart) {
        Harvesting.sneakingAtStart = sneakingAtStart;
    }
    
    static boolean sneakingAtStart;
    
    public static BlockPos getOriginBlock() {
        return originBlock;
    }
    
    public static void setOriginBlock(BlockPos originBlock) {
        Harvesting.originBlock = originBlock;
    }
    
    static BlockPos originBlock;
    
    public static void initialize() {
        Identifier modifierId = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "tree_and_ore_harvesting");
        
        PlayerStartBreakingEvent.EVENT.register((world, pos, blockState, player) -> {
            double amountToDecrease = Math.min(0.85, getBlockAmount(world, pos) * (player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).getValue() * 0.012));
            if (player instanceof ServerPlayerEntity) {
                PlayerData state = StateManager.getPlayerState(player);
                boolean meetsMining = state.skillLevels.getOrDefault("mining", 1) >= OWO_CONFIG.skillOptions.miningSettings.levelForVeinMining();
                boolean meetsWoodcutting = state.skillLevels.getOrDefault("woodcutting", 1) >= OWO_CONFIG.skillOptions.woodcuttingSettings.levelForTreeChopping();
                if (meetsWoodcutting && getWoodcuttingXpValues().stream().anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString()))) {
                    if (player.getMainHandStack().getItem() instanceof AxeItem && player.isSneaking()) {
                        setSneakingAtStart(true);
                        setOriginBlock(pos);
                        if (!player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).hasModifier(modifierId)) {
                            player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).addTemporaryModifier(new EntityAttributeModifier(modifierId, -amountToDecrease, EntityAttributeModifier.Operation.ADD_VALUE));
                            System.out.println(player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).getValue());
                        }
                    }
                }
                if (meetsMining && getMiningXpValues().stream().anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString()))) {
                    if (player.getMainHandStack().getItem() instanceof PickaxeItem && player.isSneaking()) {
                        setSneakingAtStart(true);
                        setOriginBlock(pos);
                        if (!player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).hasModifier(modifierId)) {
                            player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).addTemporaryModifier(new EntityAttributeModifier(modifierId, -amountToDecrease, EntityAttributeModifier.Operation.ADD_VALUE));
                            System.out.println(player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).getValue());
                        }
                    }
                }
            }
            return ActionResult.PASS;
        });
        
        PlayerCancelBreakingEvent.EVENT.register(((world, pos, blockState, player) -> {
            if (player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).hasModifier(modifierId)) {
                player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).removeModifier(modifierId);
            }
            return ActionResult.PASS;
        }));
        
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, blockState, blockEntity) -> {
            PlayerData state = StateManager.getPlayerState(player);
            boolean meetsMining = state.skillLevels.getOrDefault("mining", 1) >= OWO_CONFIG.skillOptions.miningSettings.levelForVeinMining();
            boolean meetsWoodcutting = state.skillLevels.getOrDefault("woodcutting", 1) >= OWO_CONFIG.skillOptions.woodcuttingSettings.levelForTreeChopping();
            if ((meetsMining || meetsWoodcutting) && player.isSneaking()) {
                return isSneakingAtStart();
            }
            return true;
        });
        
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
            PlayerData state = getPlayerState(player);
            //Checks if the block being broken is able to be vein mined and is NOT dirt, stone, or grass because mining them crashes the game. No fix known.
            if ((!blockState.getBlock().asItem().toString().contains("dirt") && !blockState.getBlock().asItem().toString().contains("grass") && !blockState.getBlock().asItem().toString().contains("stone"))) {
                List<BlockPos> blocksToBreak = getConnectedBlocks(world, pos, new ArrayList<>(), getBlockAmount(world, pos), blockState.getBlock());
                //Checks if player has at least 3 bars of hunger. Same as the amount needed to sprint.
                if (player.getHungerManager().getFoodLevel() >= 6F) {
                    /*
                    Checks if player is currently sneaking, has the minimum level req for tree harvesting, is breaking a block which woodcutting governs, and is holding an axe.
                    Then checks if the player was sneaking at the beginning of the harvesting process for that particular block.
                     */
                    if (player.isSneaking() && state.skillLevels.getOrDefault("woodcutting", 1) >= OWO_CONFIG.skillOptions.woodcuttingSettings.levelForTreeChopping() &&
                            getWoodcuttingXpValues().stream().anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString())) &&
                            player.getMainHandStack().getItem() instanceof AxeItem) {
                        if (isSneakingAtStart() && getOriginBlock().equals(pos)) {
                            for (BlockPos blockPos : blocksToBreak) {
                                getMiningXpValues().forEach(blockExperience -> {
                                    if (blockExperience.getId().equals(blockState.getBlock().asItem().toString())) {
                                        blockExperience.getExperience().forEach((skill, amount) -> {
                                            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) player, state, skill, amount / 3);
                                        });
                                    }
                                });
                                world.breakBlock(blockPos, true, player);
                                player.getHungerManager().addExhaustion(0.4F);
                                player.getMainHandStack().damage(1, player);
                            }
                        }
                    }
                    /*
                    Checks if player is currently sneaking, has the minimum level req for ore harvesting, is breaking a block which mining governs, and is holding a pickaxe.
                    Then checks if the player was sneaking at the beginning of the harvesting process for that particular block.
                     */
                    if (player.isSneaking() && state.skillLevels.getOrDefault("mining", 1) >= OWO_CONFIG.skillOptions.miningSettings.levelForVeinMining() &&
                            getMiningXpValues().stream().anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString())) &&
                            player.getMainHandStack().getItem() instanceof PickaxeItem) {
                        if (isSneakingAtStart() && getOriginBlock().equals(pos)) {
                            for (BlockPos blockPos : blocksToBreak) {
                                getMiningXpValues().forEach(blockExperience -> {
                                    if (blockExperience.getId().equals(blockState.getBlock().asItem().toString())) {
                                        blockExperience.getExperience().forEach((skill, amount) -> {
                                            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) player, state, skill, amount / 3);
                                        });
                                    }
                                });
                                world.breakBlock(blockPos, true, player);
                                player.getHungerManager().addExhaustion(0.4F);
                                player.getMainHandStack().damage(1, player);
                            }
                        }
                    }
                }
            }
            
            if (player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).hasModifier(modifierId)) {
                player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).removeModifier(modifierId);
            }
            
            setSneakingAtStart(false);
        });
    }
    
    public static int getBlockAmount(World world, BlockPos pos) {
        
        int blockAmount = 0;
        
        for (int y=1; y<=36; y+=1) {
            
            for (BlockPos bPos : BlockPos.iterate(pos.getX()-2, pos.getY()+(y-1), pos.getZ()-2, pos.getX()+2, pos.getY()+(y-1), pos.getZ()+2)) {
                BlockState bState = world.getBlockState(bPos);
                Block bBlock = bState.getBlock();
                
                if (getWoodcuttingXpValues().stream().anyMatch(block -> block.getId().equals(bBlock.asItem().toString())) || getMiningXpValues().stream().anyMatch(block -> block.getId().equals(bBlock.asItem().toString()))) {
                    blockAmount += 1;
                }
            }
        }
        return blockAmount;
    }
    
    public static List<BlockPos> getConnectedBlocks(World world, BlockPos pos, List<BlockPos> connectedBlocks, int amount, Block blockToLookFor) {
        List<BlockPos> blocksToCheck = new ArrayList<>();
        List<BlockPos> nearbyBlocks = new ArrayList<>();
        for (BlockPos connected : BlockPos.iterate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)) {
            nearbyBlocks.add(connected.toImmutable());
        }
        
        for (BlockPos blockPos : nearbyBlocks) {
            if (connectedBlocks.contains(blockPos)) {
                continue;
            }
            
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block.equals(blockToLookFor)) {
                if (blockPos.getY() != blockPos.getY() - 1) {
                    blocksToCheck.add(blockPos);
                }
                connectedBlocks.add(blockPos);
            }
        }
        
        if (blocksToCheck.isEmpty()) {
            return connectedBlocks;
        }
        
        for (BlockPos blockToCheckPos : blocksToCheck) {
            for (BlockPos blockPos : getConnectedBlocks(world, blockToCheckPos, connectedBlocks, amount, blockToLookFor)) {
                if (!connectedBlocks.contains(blockPos)) {
                    connectedBlocks.add(blockPos.toImmutable());
                }
            }
        }
        
        return getConnectedBlocks(world, pos.toImmutable(), connectedBlocks, amount, blockToLookFor);
    }
    
}
