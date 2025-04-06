package qsided.rpmechanics.skills.leveling;

import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.IncreaseSkillLevelCallback;

import java.text.DecimalFormat;

import static qsided.rpmechanics.RoleplayMechanicsCommon.sendSkillData;

public class ExperienceUp {
    public static void onExperienceUp() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        IncreaseSkillExperienceCallback.EVENT.register((player, state, skill, value) -> {
            if (state.skillLevels.getOrDefault(skill, 1) < 100) {
                state.skillExperience.put(skill, state.skillExperience.getOrDefault(skill, 0F) + value);
                state.expModifiers.forEach((id, modifier) -> {
                    if (modifier.containsKey(skill)) {
                        state.skillExperience.put(skill, state.skillExperience.getOrDefault(skill, 0F) + (value * (modifier.get(skill) / 100)));
                    }
                });
                
            }
            
            if (state.rpClassLevel < 100) {
                state.rpClassExp = state.rpClassExp + value;
                
                if (state.rpClassExp >= (300 * (state.rpClassLevel * 3.4))) {
                    state.rpClassExp = (float) (state.rpClassExp - (300 * (state.rpClassLevel * 3.4)));
                    state.rpClassLevel = state.rpClassLevel + 1;
                }
            }
            
            
            //player.sendMessage(Text.translatable("skills.ques-mod." + skill).append(Text.of(" +" + df.format(value) + "XP")).formatted(Formatting.BOLD, Formatting.GRAY), true);
            
            if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
                switch (skill) {
                    case "agility" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "swords" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.swordsOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.swordsOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.swordsOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.swordsOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.swordsOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "axes" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "bows" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "crafting" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.craftingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "enchanting" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "endurance" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enduranceOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "mining" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "woodcutting" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "smithing" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    case "farming" -> {
                        switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.multiplicativeOrAdditive()) {
                            case ADD -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                            case MULTIPLY -> {
                                if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.amount())) {
                                    IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                                }
                            }
                        }
                    }
                    
                }
            } else {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                    case ADD -> {
                        if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount())) {
                            IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                        }
                    }
                    case MULTIPLY -> {
                        if (state.skillExperience.getOrDefault(skill, 0F) >= RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (state.skillLevels.getOrDefault(skill, 1) * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount())) {
                            IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, 1, true);
                        }
                    }
                }
            }
            
            sendSkillData(state, player);
            return ActionResult.PASS;
        });
    }
}
