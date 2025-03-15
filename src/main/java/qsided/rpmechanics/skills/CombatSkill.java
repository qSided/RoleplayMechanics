package qsided.rpmechanics.skills;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CombatSkill {
    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof ServerPlayerEntity player) {
                
                PlayerData state = StateManager.getPlayerState(player);
                float combatExp = state.skillExperience.getOrDefault("combat", 0F);
                int combatLevel = state.skillLevels.getOrDefault("combat", 1);
                
                if (combatLevel < 100) {
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "combat", (12 + (killedEntity.getMaxHealth() / 4)));
                }
            }
        });
        
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
           if (entity instanceof LivingEntity livingEntity && !(entity instanceof PlayerEntity) && world.getClosestPlayer(livingEntity, 120) instanceof PlayerEntity player) {
               PlayerData state = StateManager.getPlayerState(player);
               
               ObjectMapper mapper = new ObjectMapper();
               try {
                   List<String> mobs = mapper.readValue(new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/passive_mobs.json"), new TypeReference<List<String>>() {});
                   
                   if (mobs.contains(livingEntity.getType().getName().getString().toLowerCase())) {
                       livingEntity.getAttributeInstance(EntityAttributes.MAX_HEALTH).overwritePersistentModifier(
                               new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "health_scaling"), (state.skillLevels.getOrDefault("combat", 1) * 0.4), EntityAttributeModifier.Operation.ADD_VALUE));
                       livingEntity.setHealth(livingEntity.getMaxHealth());
                       if (livingEntity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE) != null) {
                           livingEntity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).overwritePersistentModifier(
                                   new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "damage_scaling"), (state.skillLevels.getOrDefault("combat", 1) * 0.6), EntityAttributeModifier.Operation.ADD_VALUE));
                       }
                   } else {
                       livingEntity.getAttributeInstance(EntityAttributes.MAX_HEALTH).overwritePersistentModifier(
                               new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "health_scaling"), (state.skillLevels.getOrDefault("combat", 1) * 1.8), EntityAttributeModifier.Operation.ADD_VALUE));
                       livingEntity.setHealth(livingEntity.getMaxHealth());
                       if (livingEntity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE) != null) {
                           livingEntity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).overwritePersistentModifier(
                                   new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "damage_scaling"), (state.skillLevels.getOrDefault("combat", 1) * 1.15), EntityAttributeModifier.Operation.ADD_VALUE));
                       }
                       livingEntity.getAttributeInstance(EntityAttributes.ARMOR).overwritePersistentModifier(
                               new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "armor_scaling"), (state.skillLevels.getOrDefault("combat", 1) * 0.2), EntityAttributeModifier.Operation.ADD_VALUE));
                       livingEntity.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).overwritePersistentModifier(
                               new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "armor_toughness_scaling"), (state.skillLevels.getOrDefault("combat", 1) * 0.1), EntityAttributeModifier.Operation.ADD_VALUE));
                       livingEntity.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).overwritePersistentModifier(
                               new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "knockback_scaling"), (state.skillLevels.getOrDefault("combat", 1) * 0.01), EntityAttributeModifier.Operation.ADD_VALUE));
                       
                   }
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }
        });
    }
}
