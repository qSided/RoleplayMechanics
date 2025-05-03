package qsided.rpmechanics.gui.skills;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import qsided.rpmechanics.RoleplayMechanicsClient;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.config.QuesConfigModel;
import qsided.rpmechanics.gui.skills.components.QuesCheckboxComponent;

import java.text.DecimalFormat;
import java.util.List;

import static qsided.rpmechanics.config.QuesConfigModel.Choices.ADD;
import static qsided.rpmechanics.config.QuesConfigModel.Choices.MULTIPLY;

public abstract class SkillScreen extends BaseOwoScreen<FlowLayout> {
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    
    @Override
    protected void build(FlowLayout root) {
        root.surface(Surface.VANILLA_TRANSLUCENT);
        
        root.child(
                Containers.horizontalFlow(Sizing.fixed(540), Sizing.fixed(320))
                        .child(
                                Containers.verticalFlow(Sizing.fixed(110), Sizing.fixed(120))
                                        .verticalAlignment(VerticalAlignment.BOTTOM)
                                        .surface(Surface.DARK_PANEL)
                                        .padding(Insets.of(6, 6, 6, 14))
                                        .positioning(Positioning.absolute(36, 70))
                                        .id("skill-selection"))
                        .child(
                                Containers.verticalFlow(Sizing.fixed(110), Sizing.fixed(120))
                                        .verticalAlignment(VerticalAlignment.BOTTOM)
                                        .surface(Surface.DARK_PANEL)
                                        .padding(Insets.of(6, 6, 14, 6))
                                        .positioning(Positioning.absolute(396, 70))
                                        .id("milestones"))
                        .child(
                                Containers.horizontalFlow(Sizing.fill(50), Sizing.fill(70))
                                        .surface(Surface.DARK_PANEL)
                                        .padding(Insets.of(6))
                                        .positioning(Positioning.absolute(136, 50))
                                        .id("main-container")
                        )
                        .positioning(Positioning.relative(50,50))
        );
        
        drawSkillBanner(root);
        drawSkillSelection(root);
        drawMilestones(root);
        drawSkillDescription(root);
        drawSkillStats(root);
    }
    
    protected void drawSkillBanner(FlowLayout root) {
        root.childById(FlowLayout.class, "main-container")
                .child(
                        Components.item(skillIcon())
                                .sizing(Sizing.fixed(80), Sizing.fixed(80))
                                .positioning(Positioning.relative(50, 0))
                );
    }
    
    protected void drawSkillDescription(FlowLayout root) {
        root.childById(FlowLayout.class, "main-container")
                .child(
                        Components.label(skillDescription())
                                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                .maxWidth(214)
                                .positioning(Positioning.relative(50,50))
                );
    }
    
    protected void drawSkillStats(FlowLayout root) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        
        root.childById(FlowLayout.class, "main-container")
                .child(
                    Containers.grid(Sizing.fill(), Sizing.content(), infoRowCount(), 2)
                            .child(Components.label(Text.translatable("skills.rpmechanics.current_level")), 0, 0)
                            .child(
                                    Components.label(Text.of(String.valueOf(skillLevel())))
                                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                            .sizing(Sizing.fill(50), Sizing.content()),
                                    0,
                                    1
                            )
                            .child(
                                    Components.label(Text.translatable("skills.rpmechanics.current_experience")),
                                    1,
                                    0)
                            .padding(Insets.of(6))
                            .surface(Surface.flat(java.awt.Color.decode("#0b0b0b").getRGB()).and(Surface.outline(java.awt.Color.decode("#141313").getRGB())))
                            .positioning(Positioning.relative(0, 100))
                            .id("skill-stats")
                );
        if (skillLevel() < 100) {
            root.childById(GridLayout.class, "skill-stats")
                    .child(
                            Components.label(Text.of(df.format(skillExperience()) + "/" + expToNext()))
                                    .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                    .sizing(Sizing.fill(50), Sizing.content()),
                            1,
                            1
                    );
        } else {
            root.childById(GridLayout.class, "skill-stats")
            .child(
                    Components.label(Text.translatable("skills.rpmechanics.max_level"))
                            .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                            .sizing(Sizing.fill(50), Sizing.content()),
                    1,
                    1
            );
        }
        if (infoRowCount() > 2) {
            
            for (int j = 2; j < infoRowCount(); j++) {
                root.childById(GridLayout.class, "skill-stats")
                        .child(Components.label(information().get(j-2).getStatTranslationKey()), j, 0)
                        .child(Components.label(information().get(j-2).getStatValueAsText())
                                .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                .sizing(Sizing.fill(50), Sizing.content()), j, 1);
            }
        }
    }
    
    protected void drawSkillSelection(FlowLayout root) {
        root.childById(FlowLayout.class, "skill-selection")
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.select_skill"))
                                .tooltip(Text.translatable("skills.rpmechanics.select_tooltip"))
                                .positioning(Positioning.relative(50, 0))
                )
                .child(
                        Containers.verticalScroll(Sizing.fill(), Sizing.fill(92), skillSelection())
                );
    }
    
    protected void drawMilestones(FlowLayout root) {
        
        root.childById(FlowLayout.class, "milestones")
                .child(
                        Components.label(Text.translatable("skills.rpmechanics.milestones"))
                                .positioning(Positioning.relative(50, 0))
                )
                .child(
                        Containers.verticalScroll(Sizing.fill(), Sizing.fill(92),
                                Containers.grid(Sizing.fill(), Sizing.fill(), milestoneRowCount(), 3)
                                        .id("milestone")
                                )
                                .surface(Surface.flat(java.awt.Color.decode("#0b0b0b").getRGB()).and(Surface.outline(java.awt.Color.decode("#141313").getRGB())))
                                .positioning(Positioning.relative(100, 100))
                );
        
        if (!milestones().isEmpty()) {
            
            for (int j = 0; j < milestoneRowCount(); j++) {
                root.childById(GridLayout.class, "skill-stats")
                        .child(new QuesCheckboxComponent(Text.empty()).checked(hasMetMilestone(milestones().get(j).getRequiredLevel()))
                                .sizing(Sizing.fill(12), Sizing.content()), 0, 0)
                        .child(Components.label(milestones().get(j).getTranslationKey())
                                .sizing(Sizing.fill(76), Sizing.content()), j, 1)
                        .child(Components.label(Text.literal(String.valueOf(milestones().get(j).getRequiredLevel())))
                                .sizing(Sizing.fill(12), Sizing.content()), j, 2);
            }
            
        }
    }
    
    private Component skillSelection() {
        return Components.dropdown(Sizing.fill())
                
                        .button(Text.translatable("skills.rpmechanics.agility"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("agility");
                            client.setScreen(new AgilityScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.axes"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("axes");
                            client.setScreen(new AxesScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.bows"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("bows");
                            client.setScreen(new BowsSkillScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.cooking"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("cooking");
                            client.setScreen(new CookingSkillScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.crafting"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("crafting");
                            client.setScreen(new CraftingSkillScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.enchanting"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("enchanting");
                            client.setScreen(new EnchantingSkillScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.endurance"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("endurance");
                            client.setScreen(new EnduranceSkillScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.farming"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("farming");
                            client.setScreen(new FarmingSkillScreen());
                        })
                        
                        .divider()
                        
                        .button(Text.translatable("skills.rpmechanics.mining"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("mining");
                            client.setScreen(new MiningSkillScreen());
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
                        
                        .button(Text.translatable("skills.rpmechanics.woodcutting"), button -> {
                            RoleplayMechanicsClient.setLastScreenOpen("woodcutting");
                            client.setScreen(new WoodcuttingSkillScreen());
                        });
    }
    
    protected float expToNext() {
        if (!RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.useGlobal()) {
            switch (increaseMethod()) {
                case ADD -> {
                    return (float) (baseExpReq() + (skillLevel() * amountToIncrease()));
                }
                
                case MULTIPLY -> {
                    return (float) (baseExpReq() * (skillLevel() * amountToIncrease()));
                }
                
                default -> throw new IllegalStateException("Unexpected value: " + increaseMethod());
            }
        } else {
            switch (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.multiplicativeOrAdditive()) {
                case ADD -> {
                    return (float) (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() + (skillLevel() * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()));
                }
                
                case MULTIPLY -> {
                    return (float) (RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.baseExperience() * (skillLevel() * RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.globalOptions.amount()));
                }
            }
        }
        return 0;
    }
    
    public static double calculatePercentageDifference(double newValue, double originalValue) {
        if (originalValue == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return ((newValue - originalValue) / originalValue) * 100;
    }
    
    protected abstract MutableText skillDescription();
    
    protected abstract ItemStack skillIcon();
    
    protected abstract int skillLevel();
    
    protected abstract float skillExperience();
    
    protected abstract int infoRowCount();
    
    protected abstract List<SkillStatistic> information();
    
    protected abstract int milestoneRowCount();
    
    protected abstract List<Milestone> milestones();
    
    protected abstract boolean hasMetMilestone(Integer reqLevel);
    
    protected abstract Float baseExpReq();
    
    protected abstract Enum<QuesConfigModel.Choices> increaseMethod();
    
    protected abstract Double amountToIncrease();
    
    
    
}
