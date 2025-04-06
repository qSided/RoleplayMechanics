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
import qsided.rpmechanics.RoleplayMechanicsClient;
import qsided.rpmechanics.RoleplayMechanicsCommon;

import java.text.DecimalFormat;

public class AxesSkillScreen extends BaseUIModelScreen<FlowLayout> {
    public static Integer axesLevel = 1;
    public static Float axesExperience = 0F;
    public AxesSkillScreen() {
        super(FlowLayout.class, DataSource.asset(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "axes")));
    }
    
    public static void setAxesLevel(int level) {
        AxesSkillScreen.axesLevel = level;
    }
    public static void setAxesExperience(float experience) {
        AxesSkillScreen.axesExperience = experience;
    }
    
    @Override
    protected void build(FlowLayout rootComponent) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        rootComponent.childById(GridLayout.class, "axes")
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_level"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                0,
                0)
                .child(
                        Components.label(Text.of(String.valueOf(axesLevel)))
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
                        Components.label(Text.translatable("skills.rpmechanics.axes.attack_damage"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                        2,
                        0)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.axes.attack_speed"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                        3,
                        0)
                .sizing(Sizing.fill(34), Sizing.content());
        
        if (axesLevel.equals(1)) {
            rootComponent.childById(GridLayout.class, "axes")
                    .child(
                            Components.label(Text.of(String.valueOf(0)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            2,
                            1);
        } else {
            rootComponent.childById(GridLayout.class, "axes")
                    .child(
                            Components.label(Text.of(String.valueOf(df.format(axesLevel * .18))))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            2,
                            1);
        }
        if (axesLevel.equals(1)) {
            rootComponent.childById(GridLayout.class, "axes")
                    .child(
                            Components.label(Text.of(String.valueOf(0)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1);
        } else {
            rootComponent.childById(GridLayout.class, "axes")
                    .child(
                            Components.label(Text.of(String.valueOf(df.format(axesLevel * .03))))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1);
        }
        
        if (axesLevel < 100) {
            
            if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "axes")
                            .child(
                                    Components.label(Text.of(df.format(axesExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.baseExperience() + (axesLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "axes")
                            .child(
                                    Components.label(Text.of(df.format(axesExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.baseExperience() * (axesLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.axesOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                }
            } else {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "axes")
                            .child(
                                    Components.label(Text.of(df.format(axesExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (axesLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "axes")
                            .child(
                                    Components.label(Text.of(df.format(axesExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (axesLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                }
            }
            
        } else {
            rootComponent.childById(GridLayout.class, "axes")
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
