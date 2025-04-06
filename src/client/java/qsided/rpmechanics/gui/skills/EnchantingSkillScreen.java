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

public class EnchantingSkillScreen extends BaseUIModelScreen<FlowLayout> {
    public static Integer enchantingLevel = 1;
    public static Float enchantingExperience = 0F;
    public EnchantingSkillScreen() {
        super(FlowLayout.class, DataSource.asset(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "enchanting")));
    }
    
    public static void setEnchantingLevel(int level) {
        EnchantingSkillScreen.enchantingLevel = level;
    }
    public static void setEnchantingExperience(float experience) {
        EnchantingSkillScreen.enchantingExperience = experience;
    }
    
    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    
    @Override
    protected void build(FlowLayout rootComponent) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        rootComponent.childById(GridLayout.class, "enchanting")
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.current_level"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                .sizing(Sizing.fill(50), Sizing.content()),
                0,
                0)
                .child(
                        Components.label(Text.of(String.valueOf(enchantingLevel)))
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
                        Components.label(Text.translatable("skills.rpmechanics.enchanting.cost"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                        .sizing(Sizing.fill(50), Sizing.content())
                                .tooltip(Text.translatable("skills.rpmechanics.enchanting.cost.tooltip")),
                        2,
                        0)
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.enchanting.modifier"))
                                .horizontalTextAlignment(HorizontalAlignment.LEFT)
                                        .sizing(Sizing.fill(50), Sizing.content())
                                .tooltip(Text.translatable("skills.rpmechanics.enchanting.modifier.tooltip")),
                        3,
                        0
                )
                .sizing(Sizing.fill(34), Sizing.content());
        
        if (isBetween(enchantingLevel, 20, 39)) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of(String.valueOf(1)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1
                    );
        } else if (isBetween(enchantingLevel, 40, 59)) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of(String.valueOf(2)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1
                    );
        } else if (isBetween(enchantingLevel, 60, 79)) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of(String.valueOf(3)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1
                    );
        } else if (isBetween(enchantingLevel, 80, 99)) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of(String.valueOf(4)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1
                    );
        } else if (enchantingLevel == 100) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of(String.valueOf(5)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1
                    );
        } else {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of(String.valueOf(0)))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            3,
                            1
                    );
        }
        
        if (isBetween(enchantingLevel, 0, 32)) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of("-" + 0))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            2,
                            1);
        } else if (isBetween(enchantingLevel, 33, 65)) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of("-" + 1))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            2,
                            1);
        } else if (isBetween(enchantingLevel, 66, 100)) {
            rootComponent.childById(GridLayout.class, "enchanting")
                    .child(
                            Components.label(Text.of("-" + 2))
                                    .color(Color.ofArgb(0xd1d0cd))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            2,
                            1);
        }
        
        if (enchantingLevel < 100) {
            
            if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "enchanting")
                            .child(
                                    Components.label(Text.of(df.format(enchantingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.baseExperience() + (enchantingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "enchanting")
                            .child(
                                    Components.label(Text.of(df.format(enchantingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.baseExperience() * (enchantingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                }
            } else {
                switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                    case ADD -> rootComponent.childById(GridLayout.class, "enchanting")
                            .child(
                                    Components.label(Text.of(df.format(enchantingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (enchantingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                    case MULTIPLY -> rootComponent.childById(GridLayout.class, "enchanting")
                            .child(
                                    Components.label(Text.of(df.format(enchantingExperience) + "/" + df.format(RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (enchantingLevel * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()))))
                                            .color(Color.ofArgb(0xd1d0cd))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    1,
                                    1);
                }
            }
            
        } else {
            rootComponent.childById(GridLayout.class, "enchanting")
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
                .button(Text.translatable("skills.rpmechanics.combat"), button -> {
                    RoleplayMechanicsClient.setLastScreenOpen("combat");
                    client.setScreen(new SwordsSkillScreen());
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
