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
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.RoleplayMechanicsClient;

import java.text.DecimalFormat;

public class MiningSkillScreen extends BaseUIModelScreen<FlowLayout> {
    public static Integer miningLevel = 1;
    public static Float miningExperience = 0F;
    public MiningSkillScreen() {
        super(FlowLayout.class, DataSource.asset(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "mining")));
    }
    
    public static void setMiningLevel(int level) {
        MiningSkillScreen.miningLevel = level;
    }
    public static void setMiningExperience(float experience) {
        MiningSkillScreen.miningExperience = experience;
    }
    
    @Override
    protected void build(FlowLayout rootComponent) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        rootComponent.childById(GridLayout.class, "mining")
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_level"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT),
                0,
                0)
                .child(
                        Components.label(Text.of(String.valueOf(miningLevel)))
                                .color(Color.ofArgb(0xd1d0cd))
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                        0,
                        2)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_experience"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT),
                        1,
                        0)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.mining.efficiency"))
                            .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .tooltip(Text.translatable("skills.rpmechanics.mining.efficiency.tooltip")),
                        2,
                        0)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.mining.degrade_chance"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .tooltip(Text.translatable("skills.rpmechanics.mining.degrade_chance.tooltip")),
                        3,
                        0)
                .child(
                        Components.label(Text.of("-" + miningLevel + "%"))
                                .color(Color.ofArgb(0xd1d0cd))
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                        3,
                        2);
        
        if (miningLevel.equals(1)) {
            rootComponent.childById(GridLayout.class, "mining")
                    .child(
                            Components.label(Text.of(String.valueOf(0)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                            2,
                            2);
        } else {
            rootComponent.childById(GridLayout.class, "mining")
                    .child(
                        Components.label(Text.of(String.valueOf(miningLevel * .5)))
                                .color(Color.ofArgb(0xd1d0cd))
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                        2,
                        2);
        }
        
        if (miningLevel < 100) {
            
            if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "mining")
                            .child(
                                    Components.label(Text.of(df.format(miningExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.baseExperience() + (miningLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "mining")
                            .child(
                                    Components.label(Text.of(df.format(miningExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.baseExperience() * (miningLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.miningOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                }
            } else {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "mining")
                            .child(
                                    Components.label(Text.of(df.format(miningExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (miningLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "mining")
                            .child(
                                    Components.label(Text.of(df.format(miningExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (miningLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT),
                                    1,
                                    2);
                }
            }
            
        } else {
            rootComponent.childById(GridLayout.class, "mining")
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
