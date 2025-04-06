package qsided.rpmechanics.skills.leveling;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.IncreaseSkillLevelCallback;
import qsided.rpmechanics.networking.LevelUpPayload;

import static qsided.rpmechanics.RoleplayMechanicsCommon.sendSkillData;

public class LevelUp {
    public static void onLevelUp() {
        IncreaseSkillLevelCallback.EVENT.register((player, state, skill, value, shouldMessage) -> {
            
            if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
                switch (skill) {
                    case "agility" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.multiplicativeOrAdditive()) {
                            case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.amount()))), 0));
                            case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.amount()))), 0));
                        }
                    }
                    //case "swords" -> {
                    //    switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.combatOptions.multiplicativeOrAdditive()) {
                    //        case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.combatOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.combatOptions.amount()))), 0));
                    //        case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.combatOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.combatOptions.amount()))), 0));
                    //    }
                    //}
                    case "crafting" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.multiplicativeOrAdditive()) {
                            case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.amount()))), 0));
                            case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.amount()))), 0));
                        }
                    }
                    case "enchanting" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.multiplicativeOrAdditive()) {
                            case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.amount()))), 0));
                            case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.amount()))), 0));
                        }
                    }
                    case "endurance" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.multiplicativeOrAdditive()) {
                            case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.amount()))), 0));
                            case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.amount()))), 0));
                        }
                    }
                    case "mining" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.multiplicativeOrAdditive()) {
                            case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.amount()))), 0));
                            case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.amount()))), 0));
                        }
                    }
                    case "woodcutting" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.multiplicativeOrAdditive()) {
                            case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.amount()))), 0));
                            case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.amount()))), 0));
                        }
                    }
                    
                }
            } else {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                    case ADD -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))), 0));
                    case MULTIPLY -> state.skillExperience.put(skill, (float) Math.max((state.skillExperience.getOrDefault(skill, 0F) - (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))), 0));
                }
            }
            
            state.skillLevels.put(skill, state.skillLevels.getOrDefault(skill, 1) + value);
            ServerPlayNetworking.send(player, new LevelUpPayload(skill, state.skillLevels.get(skill), shouldMessage));
            
            switch (skill) {
                case "mining" -> {
                    if (state.skillLevels.getOrDefault(skill, 1) == 33) {
                        player.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED).overwritePersistentModifier(
                                new EntityAttributeModifier(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "mining_one"), 10,
                                        EntityAttributeModifier.Operation.ADD_VALUE)
                        );
                    }
                }
                case "swords" -> {
                    Identifier combatModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "combat_modifier");
                    
                    player.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).overwritePersistentModifier(
                            new EntityAttributeModifier(combatModifier, state.skillLevels.getOrDefault(skill, 1) * .18,
                                    EntityAttributeModifier.Operation.ADD_VALUE)
                    );
                    player.getAttributeInstance(EntityAttributes.ATTACK_SPEED).overwritePersistentModifier(
                            new EntityAttributeModifier(combatModifier, state.skillLevels.getOrDefault(skill, 1) * .03,
                                    EntityAttributeModifier.Operation.ADD_VALUE)
                    );
                }
                case "endurance" -> {
                    Identifier enduranceModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "endurance_modifier");
                    
                    player.getAttributeInstance(EntityAttributes.MAX_HEALTH).overwritePersistentModifier(
                            new EntityAttributeModifier(enduranceModifier, state.skillLevels.getOrDefault(skill, 1) * 2,
                                    EntityAttributeModifier.Operation.ADD_VALUE)
                    );
                }
                case "agility" -> {
                    Identifier agilityModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "agility_modifier");
                    
                    player.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).overwritePersistentModifier(
                            new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault(skill, 1) * 0.001, EntityAttributeModifier.Operation.ADD_VALUE)
                    );
                    player.getAttributeInstance(EntityAttributes.SAFE_FALL_DISTANCE).overwritePersistentModifier(
                            new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault(skill, 1) * 0.1, EntityAttributeModifier.Operation.ADD_VALUE)
                    );
                    player.getAttributeInstance(EntityAttributes.JUMP_STRENGTH).overwritePersistentModifier(
                            new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault(skill, 1) * 0.0058, EntityAttributeModifier.Operation.ADD_VALUE)
                    );
                }
            }
            
            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, skill, 0F);
            
            sendSkillData(state, player);
            return ActionResult.PASS;
        });
    }
}
