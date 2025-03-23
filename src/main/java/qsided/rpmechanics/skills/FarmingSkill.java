package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

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
                                player.sendMessage(Text.of("Awarded " + value + " experience for harvesting"), false);
                            });
                        }
                        if (blockState.getBlock() instanceof StemBlock stemBlock && blockState.equals(stemBlock.getDefaultState().with(Properties.AGE_7, 7))) {
                            block.getExperience().forEach((skill, value) -> {
                                IncreaseSkillExperienceCallback.EVENT.invoker()
                                        .increaseExp((ServerPlayerEntity) player, state, skill, value);
                                player.sendMessage(Text.of("Awarded " + value + " experience for harvesting"), false);
                            });
                        }
                    }
                });
            }
        });
        
        UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
            if (!playerEntity.isSpectator()) {
                if (playerEntity instanceof ServerPlayerEntity player) {
                    if (world.getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof FarmlandBlock) {
                        PlayerData state = getPlayerState(player);
                    
                        RoleplayMechanicsCommon.getFarmingXpValues().forEach(blockExperience -> {
                            if (player.getStackInHand(hand).getItem().toString().equals(blockExperience.getId())) {
                                blockExperience.getExperience().forEach((skill, value) -> {
                                    IncreaseSkillExperienceCallback.EVENT.invoker()
                                            .increaseExp(player, state, skill, value);
                                    player.sendMessage(Text.of("Awarded " + value + " experience for planting"), false);
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
