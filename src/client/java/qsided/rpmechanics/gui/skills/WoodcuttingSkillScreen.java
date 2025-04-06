package qsided.rpmechanics.gui.skills;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.DropdownComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.RoleplayMechanicsClient;

import java.text.DecimalFormat;

public class WoodcuttingSkillScreen extends BaseUIModelScreen<FlowLayout> {
    public static Integer woodcuttingLevel = 1;
    public static Float woodcuttingExperience = 0F;
    public WoodcuttingSkillScreen() {
        super(FlowLayout.class, DataSource.asset(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "woodcutting")));
    }
    
    public static void setWoodcuttingLevel(int level) {
        WoodcuttingSkillScreen.woodcuttingLevel = level;
    }
    public static void setWoodcuttingExperience(float experience) {
        WoodcuttingSkillScreen.woodcuttingExperience = experience;
    }
    
    @Override
    protected void build(FlowLayout rootComponent) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        rootComponent.childById(GridLayout.class, "woodcutting")
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_level"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                0,
                0)
                .child(
                        Components.label(Text.of(String.valueOf(woodcuttingLevel)))
                                .color(Color.ofArgb(0xd1d0cd))
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                        0,
                        1)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_experience"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                        1,
                        0)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.woodcutting.efficiency"))
                            .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                        2,
                        0)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.woodcutting.extra_chance"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                        3,
                        0)
                .child(
                        Components.label(Text.of(woodcuttingLevel + "%"))
                                .color(Color.ofArgb(0xd1d0cd))
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                        3,
                        1)
                .sizing(Sizing.fill(34), Sizing.content());
        
        if (woodcuttingLevel.equals(1)) {
            rootComponent.childById(GridLayout.class, "woodcutting")
                    .child(
                            Components.label(Text.of(String.valueOf(0)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            2,
                            1);
        } else {
            rootComponent.childById(GridLayout.class, "woodcutting")
                    .child(
                        Components.label(Text.of(String.valueOf(woodcuttingLevel * .5)))
                                .color(Color.ofArgb(0xd1d0cd))
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                        2,
                        1);
        }
        
        if (woodcuttingLevel < 100) {
            
            if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "woodcutting")
                            .child(
                                    Components.label(Text.of(df.format(woodcuttingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.baseExperience() + (woodcuttingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "woodcutting")
                            .child(
                                    Components.label(Text.of(df.format(woodcuttingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.baseExperience() * (woodcuttingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                }
            } else {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "woodcutting")
                            .child(
                                    Components.label(Text.of(df.format(woodcuttingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (woodcuttingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "woodcutting")
                            .child(
                                    Components.label(Text.of(df.format(woodcuttingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (woodcuttingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                }
            }
            
        } else {
            rootComponent.childById(GridLayout.class, "woodcutting")
                    .child(
                            Components.label(Text.translatable("skills.rpmechanics.max_level"))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            1,
                            1);
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
                .button(Text.translatable("skills.rpmechanics.farming"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("farming");
                    client.setScreen(new FarmingSkillScreen());
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
                .button(Text.translatable("skills.rpmechanics.swords"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("swords");
                    client.setScreen(new SwordsSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.axes"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("axes");
                    client.setScreen(new AxesSkillScreen());
                })
                .divider()
                .button(Text.translatable("skills.rpmechanics.bows"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("bows");
                    client.setScreen(new BowsSkillScreen());
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
