package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.Random;

import static qsided.rpmechanics.StateManager.getPlayerState;

public class FarmingSkill {
    
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, blockPos, blockState, blockEntity) -> {
            if (player instanceof ServerPlayerEntity) {
                PlayerData state = getPlayerState(player);
                
                RoleplayMechanicsCommon.getFarmingXpValues().forEach(block -> {
                    if (blockState.getBlock().asItem().toString().equals(block.getId())) {
                        if (blockState.getBlock() instanceof CropBlock cropBlock && cropBlock.isMature(blockState)) {
                            block.getExperience().forEach((skill, value) -> {
                                IncreaseSkillExperienceCallback.EVENT.invoker()
                                        .increaseExp((ServerPlayerEntity) player, state, skill, value);
                            });
                        }
                        if (blockState.getBlock() instanceof StemBlock stemBlock && blockState.equals(stemBlock.getDefaultState().with(Properties.AGE_7, 7))) {
                            block.getExperience().forEach((skill, value) -> {
                                IncreaseSkillExperienceCallback.EVENT.invoker()
                                        .increaseExp((ServerPlayerEntity) player, state, skill, value);
                            });
                        }
                    }
                });
                
                Random r = new Random();
                int randomInt = r.nextInt(100) + 1;
                
                if (randomInt <= state.skillLevels.getOrDefault("farming", 1) && RoleplayMechanicsCommon.getFarmingXpValues().stream().anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString()))) {
                    Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, blockEntity).forEach(stack -> {
                        Block.dropStack(world, blockPos, stack);
                    });
                }
            }
        });
        
        UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
            if (!playerEntity.isSpectator()) {
                if (playerEntity instanceof ServerPlayerEntity player) {
                    PlayerData state = getPlayerState(player);
                    if (world.getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof FarmlandBlock) {
                        
                    
                        RoleplayMechanicsCommon.getFarmingXpValues().forEach(blockExperience -> {
                            if (player.getStackInHand(hand).getItem().toString().equals(blockExperience.getId())) {
                                blockExperience.getExperience().forEach((skill, value) -> {
                                    IncreaseSkillExperienceCallback.EVENT.invoker()
                                            .increaseExp(player, state, skill, value / 4);
                                });
                            }
                        });
                    }
                    if (world.getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof CropBlock crop
                            && state.skillLevels.getOrDefault("farming", 1) >= 10
                            && crop.isMature(world.getBlockState(blockHitResult.getBlockPos()))) {
                        BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
                        ItemStack stack = blockState.getBlock().asItem().getDefaultStack();
                        PlayerInventory inventory = player.getInventory();
                        world.breakBlock(blockHitResult.getBlockPos(), true, player);
                        if (inventory.contains(stack)) {
                            inventory.removeStack(inventory.getSlotWithStack(stack), 1);
                            world.setBlockState(blockHitResult.getBlockPos(), blockState.getBlock().getDefaultState());
                        }
                        
                        RoleplayMechanicsCommon.getFarmingXpValues().forEach(block -> {
                            if (world.getBlockState(blockHitResult.getBlockPos()).getBlock().asItem().toString().equals(block.getId())) {
                                block.getExperience().forEach((skill, value) -> {
                                    IncreaseSkillExperienceCallback.EVENT.invoker()
                                            .increaseExp(player, state, skill, value);
                                });
                            }
                        });
                    }
                    
                }
                
                return ActionResult.PASS;
            } else {
                return ActionResult.FAIL;
            }
        });
    }
}
