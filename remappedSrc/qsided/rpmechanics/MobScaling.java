package qsided.rpmechanics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MobScaling {
    
    public static void initialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity livingEntity && !(entity instanceof PlayerEntity) && world.getClosestPlayer(livingEntity, 120) instanceof PlayerEntity player) {
                PlayerData state = StateManager.getPlayerState(player);
                
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<String> mobs = mapper.readValue(new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/passive_mobs.json"), new TypeReference<List<String>>() {});
                    
                    if (mobs.contains(livingEntity.getType().getName().getString().toLowerCase())) {
                        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).overwritePersistentModifier(
                                new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "health_scaling"), (state.rpClassLevel * 0.4), EntityAttributeModifier.Operation.ADD_VALUE));
                        livingEntity.setHealth(livingEntity.getMaxHealth());
                        if (livingEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
                            livingEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).overwritePersistentModifier(
                                    new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "damage_scaling"), (state.rpClassLevel * 0.6), EntityAttributeModifier.Operation.ADD_VALUE));
                        }
                    } else {
                        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).overwritePersistentModifier(
                                new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "health_scaling"), (state.rpClassLevel * 1.8), EntityAttributeModifier.Operation.ADD_VALUE));
                        livingEntity.setHealth(livingEntity.getMaxHealth());
                        if (livingEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
                            livingEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).overwritePersistentModifier(
                                    new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "damage_scaling"), (state.rpClassLevel * 1.15), EntityAttributeModifier.Operation.ADD_VALUE));
                        }
                        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).overwritePersistentModifier(
                                new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "armor_scaling"), (state.rpClassLevel * 0.2), EntityAttributeModifier.Operation.ADD_VALUE));
                        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).overwritePersistentModifier(
                                new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "armor_toughness_scaling"), (state.rpClassLevel * 0.1), EntityAttributeModifier.Operation.ADD_VALUE));
                        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).overwritePersistentModifier(
                                new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "knockback_scaling"), (state.rpClassLevel * 0.01), EntityAttributeModifier.Operation.ADD_VALUE));
                        
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
}
