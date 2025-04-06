package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.Random;

import static qsided.rpmechanics.RoleplayMechanicsCommon.getWoodcuttingXpValues;
import static qsided.rpmechanics.StateManager.getPlayerState;

public class WoodcuttingSkill {
    
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
            PlayerData state = getPlayerState(player);
            getWoodcuttingXpValues().forEach(block -> {
                if (blockState.getBlock().asItem().toString().equals(block.getId())) {
                    
                    block.getExperience().forEach((skill, value) -> {
                        IncreaseSkillExperienceCallback.EVENT.invoker()
                                .increaseExp((ServerPlayerEntity) player, state, skill, value);
                    });
                    
                }
            });
            
            Random r = new Random();
            int randomInt = r.nextInt(100) + 1;
            if (randomInt <= state.skillLevels.getOrDefault("woodcutting", 1) && getWoodcuttingXpValues().stream().anyMatch(blockExperience -> blockExperience.getId().equals(blockState.getBlock().asItem().toString()))) {
                Block.getDroppedStacks(blockState, (ServerWorld) world, pos, blockEntity).forEach(stack -> {
                    Block.dropStack(world, pos, stack);
                });
            }
        });
    }
    
}