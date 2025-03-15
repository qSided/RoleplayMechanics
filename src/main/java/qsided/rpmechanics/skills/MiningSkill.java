package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.Random;

import static qsided.rpmechanics.StateManager.getPlayerState;

public class MiningSkill {
    
    public static void register() {
        Random r = new Random();
        int randomInt = r.nextInt(100) + 1;
        
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
            
            PlayerData state = getPlayerState(player);
            int miningLevel = state.skillLevels.getOrDefault("mining", 1);
            
            RoleplayMechanicsCommon.getBlockXpValues().forEach(block -> {
                if (blockState.getBlock().asItem().toString().equals(block.getId())) {
                    
                    block.getExperience().forEach((skill, value) -> {
                        IncreaseSkillExperienceCallback.EVENT.invoker()
                                .increaseExp((ServerPlayerEntity) player, state, skill, value);
                    });
                }
            });
            
            
            if (player.getEquippedStack(EquipmentSlot.MAINHAND).isIn(ItemTags.PICKAXES) || player.getEquippedStack(EquipmentSlot.MAINHAND).isIn(ItemTags.SHOVELS)
                    && player.getEquippedStack(EquipmentSlot.MAINHAND).isDamaged() && randomInt <= miningLevel) {
                player.getEquippedStack(EquipmentSlot.MAINHAND).setDamage(Math.max(player.getEquippedStack(EquipmentSlot.MAINHAND).getDamage() - 1, 0));
            }
        });
    }
}
