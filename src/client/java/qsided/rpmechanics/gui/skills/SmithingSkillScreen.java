package qsided.rpmechanics.gui.skills;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.DropdownComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsClient;
import qsided.rpmechanics.RoleplayMechanicsCommon;

import java.text.DecimalFormat;

public class SmithingSkillScreen extends BaseUIModelScreen<FlowLayout> {
    public static Integer smithingLevel = 1;
    public static Float smithingExperience = 0F;
    
    public SmithingSkillScreen() {
        super(FlowLayout.class, DataSource.asset(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "smithing")));
    }
    
    public static void setSmithingLevel(int level) {
        SmithingSkillScreen.smithingLevel = level;
    }
    public static void setSmithingExperience(float experience) {
        SmithingSkillScreen.smithingExperience = experience;
    }
    
    @Override
    protected void build(FlowLayout rootComponent) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        rootComponent.childById(GridLayout.class, "smithing")
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_level"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT),
                0,
                0)
                .child(
                        Components.label(Text.of(String.valueOf(smithingLevel)))
                                .color(Color.ofArgb(0xd1d0cd))
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                        0,
                        2)
                
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_experience"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT),
                        1,
                        0);
        
        if (smithingLevel < 100) {
            
            if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "smithing")
                            .child(
                                    Components.label(Text.of(df.format(smithingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.baseExperience() + (smithingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "smithing")
                            .child(
                                    Components.label(Text.of(df.format(smithingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.baseExperience() * (smithingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.smithingOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                }
            } else {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "smithing")
                            .child(
                                    Components.label(Text.of(df.format(smithingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (smithingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "smithing")
                            .child(
                                    Components.label(Text.of(df.format(smithingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (smithingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                }
            }
            
        } else {
            rootComponent.childById(GridLayout.class, "smithing")
                    .child(
                            Components.label(Text.translatable("skills.rpmechanics.max_level"))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                            1,
                            2);
        }
        
        rootComponent.childById(DropdownComponent.class, "skill-selection")
                .text(Text.translatable("skills.rpmechanics.select_skill"))
                .divider()
                .button(Text.translatable("skills.rpmechanics.mining"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("mining");
                    client.setScreen(new MiningSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.woodcutting"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("woodcutting");
                    client.setScreen(new WoodcuttingSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.enchanting"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("enchanting");
                    client.setScreen(new EnchantingSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.crafting"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("crafting");
                    client.setScreen(new CraftingSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.smithing"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("smithing");
                    client.setScreen(new SmithingSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.combat"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("combat");
                    client.setScreen(new CombatSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.endurance"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("endurance");
                    client.setScreen(new EnduranceSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.agility"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("agility");
                    client.setScreen(new AgilitySkillScreen());
                })
                .positioning(Positioning.absolute(0, 0));
    }
}
