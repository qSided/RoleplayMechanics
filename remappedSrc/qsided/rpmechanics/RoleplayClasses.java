package qsided.rpmechanics;

import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.config.roleplay_classes.RoleplayClass;
import qsided.rpmechanics.config.roleplay_classes.SkillModifier;
import qsided.rpmechanics.config.roleplay_classes.StartingEquipment;
import qsided.rpmechanics.config.roleplay_classes.StartingSkill;
import qsided.rpmechanics.events.IncreaseSkillLevelCallback;
import qsided.rpmechanics.events.RoleplayClassSelectedCallback;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class RoleplayClasses {
    public static final RoleplayClass PALADIN;
    public static final RoleplayClass RANGER;
    public static final RoleplayClass FIGHTER;
    public static final RoleplayClass ARTISAN;
    public static final RoleplayClass FARMER;
    
    public static void initialize() {
        RoleplayClassSelectedCallback.EVENT.register((player, state, rpClassId) -> {
            
            if (state.rpClass.equals("")) {
                player.sendMessage(Text.translatable("classes.rpmechanics.class_selected")
                    .append(Text.literal(
                            RoleplayMechanicsCommon.getRpClasses().get(rpClassId).getName()).getWithStyle(Style.EMPTY.withColor(Color.decode(RoleplayMechanicsCommon.getRpClasses().get(rpClassId).getColor()).getRGB())).getFirst()
                    )
                );
                
                RoleplayMechanicsCommon.getRpClasses().get(rpClassId).getStartingSkills().forEach(startingSkill -> IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, startingSkill.getSkill(), startingSkill.getLevel(), false));
                
                RoleplayMechanicsCommon.getRpClasses().get(rpClassId).getStartingEquipment().forEach(startingEquipment -> {
                    for (int i = 0; i<startingEquipment.getCount(); i++) {
                        player.giveItemStack(Registries.ITEM.get(Identifier.of(startingEquipment.getItem())).getDefaultStack());
                    }
                });
                
                HashMap<String, Integer> modifiers = new HashMap<>();
                RoleplayMechanicsCommon.getRpClasses().get(rpClassId).getSkillModifiers().forEach(skillModifier -> {
                    modifiers.put(skillModifier.getSkill(), skillModifier.getPercentage());
                });
                state.expModifiers.put(RoleplayMechanicsCommon.getRpClasses().get(rpClassId).getName(), modifiers);
                
                state.rpClass = RoleplayMechanicsCommon.getRpClasses().get(rpClassId).getName();
            } else {
                player.sendMessage(Text.translatable("classes.rpmechanics.already_selected"));
            }
            
            
            
            return ActionResult.PASS;
        });
    }
    
    static {
        PALADIN =
                new RoleplayClass(
                        "Paladin",
                        "#1a3078",
                        "Paladins train their bodies, minds, and spirits in order to protect themselves and their party. Some Paladins even wield powerful magic to buff themselves and their party.",
                        List.of(new StartingEquipment(Items.LEATHER_HELMET.toString(), 1),
                                new StartingEquipment(Items.LEATHER_CHESTPLATE.toString(), 1),
                                new StartingEquipment(Items.LEATHER_LEGGINGS.toString(), 1),
                                new StartingEquipment(Items.LEATHER_BOOTS.toString(), 1),
                                new StartingEquipment(Items.WOODEN_SWORD.toString(), 1),
                                new StartingEquipment(Items.SHIELD.toString(), 1),
                                new StartingEquipment(Items.COOKED_BEEF.toString(), 12)),
                        
                        List.of(new StartingSkill("endurance", 10),
                                new StartingSkill("swords", 5)),
                        
                        List.of(new SkillModifier("endurance", 10),
                                new SkillModifier("swords", 5)));
        RANGER =
                new RoleplayClass(
                        "Ranger",
                        "#16703a",
                        "Needs finished! Got a suggestion for this? Join the Discord! This can also be edited in the config.",
                        List.of(new StartingEquipment(Items.LEATHER_CHESTPLATE.toString(), 1),
                                new StartingEquipment(Items.BOW.toString(), 1),
                                new StartingEquipment(Items.ARROW.toString(), 64),
                                new StartingEquipment(Items.PUMPKIN_PIE.toString(), 12)),
                        
                        List.of(new StartingSkill("agility", 10),
                                new StartingSkill("bows", 5)),
                        
                        List.of(new SkillModifier("agility", 10),
                                new SkillModifier("bows", 10)));
        FIGHTER =
                new RoleplayClass(
                        "Fighter",
                        "#8f242b",
                        "Whether it's a bar brawl or an all out war, Fighters live for battle. While not skilled in magic or defense, Fighters are quite agile and hit hard.",
                        List.of(new StartingEquipment(Items.LEATHER_CHESTPLATE.toString(), 1),
                                new StartingEquipment(Items.WOODEN_SWORD.toString(), 1),
                                new StartingEquipment(Items.WOODEN_AXE.toString(), 1),
                                new StartingEquipment(Items.COOKED_PORKCHOP.toString(), 12)),
                        
                        List.of(new StartingSkill("axes", 10),
                                new StartingSkill("agility", 5)),
                        
                        List.of(new SkillModifier("axes", 10),
                                new SkillModifier("agility", 5)));
        ARTISAN =
                new RoleplayClass(
                        "Artisan",
                        "#fcca03",
                        "Skilled in forging weapons and tools.",
                        List.of(new StartingEquipment(Items.IRON_BLOCK.toString(), 4),
                                new StartingEquipment(Items.CRAFTING_TABLE.toString(), 1),
                                new StartingEquipment(Items.WOODEN_PICKAXE.toString(), 1),
                                new StartingEquipment(Items.COOKED_PORKCHOP.toString(), 12)),
                        
                        List.of(new StartingSkill("crafting", 10),
                                new StartingSkill("smithing", 10)),
                        
                        List.of(new SkillModifier("crafting", 10),
                                new SkillModifier("smithing", 10)));
        FARMER =
                new RoleplayClass(
                        "Farmer",
                        "#a87147",
                        "Farmers are no strangers to hard work. Years of tending to the land have taught them techniques to get the most out of a harvest.",
                        List.of(new StartingEquipment(Items.WOODEN_HOE.toString(), 1),
                                new StartingEquipment(Items.POTATO.toString(), 3),
                                new StartingEquipment(Items.CARROT.toString(), 3),
                                new StartingEquipment(Items.BEETROOT_SEEDS.toString(), 3),
                                new StartingEquipment(Items.BUCKET.toString(), 1),
                                new StartingEquipment(Items.LEATHER_HELMET.toString(), 1),
                                new StartingEquipment(Items.COOKED_BEEF.toString(), 12)),
                        
                        List.of(new StartingSkill("farming", 10),
                                new StartingSkill("endurance", 5)),
                        
                        List.of(new SkillModifier("farming", 10),
                                new SkillModifier("endurance", 5)));
        
    }
}
