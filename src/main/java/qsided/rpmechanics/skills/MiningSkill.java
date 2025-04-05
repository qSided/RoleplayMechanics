package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.PlayerStartBreakingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static qsided.rpmechanics.RoleplayMechanicsCommon.getMiningXpValues;
import static qsided.rpmechanics.StateManager.getPlayerState;

public class MiningSkill {
    
    public static void register() {
        Random r = new Random();
        int randomInt = r.nextInt(100) + 1;
        
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
            PlayerData state = getPlayerState(player);
            int miningLevel = state.skillLevels.getOrDefault("mining", 1);
            
            getMiningXpValues().forEach(block -> {
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
