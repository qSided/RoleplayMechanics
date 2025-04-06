package qsided.rpmechanics.skills.combat;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import static qsided.rpmechanics.RoleplayMechanicsCommon.OWO_CONFIG;

public class SwordsAndAxesSkills {
    
    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof ServerPlayerEntity player) {
                
                PlayerData state = StateManager.getPlayerState(player);
                
                if (player.getMainHandStack().isIn(ItemTags.SWORDS) && state.skillLevels.getOrDefault("swords", 1) < 100) {
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "swords", (12 + (killedEntity.getMaxHealth() / 4)));
                } else if (player.getMainHandStack().isIn(ItemTags.AXES) && state.skillLevels.getOrDefault("axes", 1) < 100) {
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "axes", (12 + (killedEntity.getMaxHealth() / 4)));
                }
            }
        });
        
        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previous, next) -> {
            if (livingEntity instanceof ServerPlayerEntity player) {
                PlayerData state = StateManager.getPlayerState(player);
                Integer axesLevel = state.skillLevels.getOrDefault("axes", 1);
                Integer swordsLevel = state.skillLevels.getOrDefault("swords", 1);
                Identifier axeModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "axes_skill");
                Identifier swordModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "swords_skill");
                
                if (next.isIn(ItemTags.AXES)) {
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addTemporaryModifier(
                            new EntityAttributeModifier(axeModifier, axesLevel * OWO_CONFIG.skillOptions.axesSettings.damage(), EntityAttributeModifier.Operation.ADD_VALUE));
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).addTemporaryModifier(
                            new EntityAttributeModifier(axeModifier, axesLevel * OWO_CONFIG.skillOptions.axesSettings.speed(), EntityAttributeModifier.Operation.ADD_VALUE));
                } else if (player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).hasModifier(axeModifier) || player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).hasModifier(axeModifier)) {
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).removeModifier(axeModifier);
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).removeModifier(axeModifier);
                }
                
                if (next.isIn(ItemTags.SWORDS)) {
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addTemporaryModifier(
                            new EntityAttributeModifier(swordModifier, swordsLevel * OWO_CONFIG.skillOptions.swordsSettings.damage(), EntityAttributeModifier.Operation.ADD_VALUE));
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).addTemporaryModifier(
                            new EntityAttributeModifier(swordModifier, swordsLevel * OWO_CONFIG.skillOptions.swordsSettings.speed(), EntityAttributeModifier.Operation.ADD_VALUE));
                } else if (player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).hasModifier(axeModifier) || player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).hasModifier(axeModifier)) {
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).removeModifier(axeModifier);
                    player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).removeModifier(axeModifier);
                }
            }
        });
    }
    
}
