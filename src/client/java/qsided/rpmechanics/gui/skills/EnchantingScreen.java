package qsided.rpmechanics.gui.skills;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.config.QuesConfigModel;
import qsided.rpmechanics.skills.milestones.SkillMilestone;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EnchantingScreen extends SkillScreen {
    
    public static Integer level;
    public static Float exp;
    
    public static Float getExp() {
        return exp;
    }
    
    public static void setExp(Float exp) {
        EnchantingScreen.exp = exp;
    }
    
    public static Integer getLevel() {
        return level;
    }
    
    public static void setLevel(Integer level) {
        EnchantingScreen.level = level;
    }
    
    @Override
    protected MutableText skillDescription() {
        return Text.translatable("skills.descriptions.rpmechanics.enchanting");
    }
    
    @Override
    protected int skillDescriptionOffset() {
        return 10;
    }
    
    @Override
    protected ItemStack skillIcon() {
        return Items.ENCHANTED_BOOK.getDefaultStack();
    }
    
    @Override
    protected int skillLevel() {
        return getLevel();
    }
    
    @Override
    protected float skillExperience() {
        return getExp();
    }
    
    @Override
    protected int infoRowCount() {
        return 4;
    }
    
    @Override
    protected List<SkillStatistic> information() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        List<SkillStatistic> stats = new ArrayList<>();
        
        stats.add(new SkillStatistic(Text.translatable("skills.rpmechanics.enchanting.cost"), Text.literal("-" + costReduction(skillLevel()))));
        stats.add(new SkillStatistic(Text.translatable("skills.rpmechanics.enchanting.modifier"), Text.literal("+" + modifier(skillLevel()) + " Power")));
        
        return stats;
    }
    
    private static int costReduction(Integer level) {
        int costReduction = 0;
        for (SkillMilestone skillMilestone : RoleplayMechanicsCommon.getMilestones()) {
            if (skillMilestone.hasMet(level) &&
                    skillMilestone.getSkill().equals("enchanting") &&
                    skillMilestone.getRewardType().equals("ENCHANT_COST_REDUCTION") &&
                    skillMilestone.getRewardAmount() != null) {
                costReduction += skillMilestone.getRewardAmount();
            }
        }
        
        return costReduction;
    }
    private static int modifier (Integer level) {
        int powerModifier = 0;
        for (SkillMilestone skillMilestone : RoleplayMechanicsCommon.getMilestones()) {
            if (skillMilestone.hasMet(level) &&
                    skillMilestone.getSkill().equals("enchanting") &&
                    skillMilestone.getRewardType().equals("ENCHANT_POWER") &&
                    skillMilestone.getRewardAmount() != null) {
                powerModifier += skillMilestone.getRewardAmount();
            }
        }
        
        return powerModifier;
    }
    
    @Override
    protected int milestoneCount() {
        return (int) RoleplayMechanicsCommon.getMilestones().stream().filter(skillMilestone -> skillMilestone.getSkill().equals("enchanting")).count();
    }
    
    @Override
    protected List<Milestone> milestones() {
        List<Milestone> milestones = new ArrayList<>();
        
        RoleplayMechanicsCommon.getMilestones()
                .stream()
                .filter(
                        skillMilestone -> skillMilestone.getSkill().equals("enchanting")
                )
                .forEach(
                        skillMilestone -> milestones.add(new Milestone(skillMilestone.getLevelReq(), Text.literal(skillMilestone.getText())))
                );
        
        return milestones;
    }
    
    @Override
    protected Float baseExpReq() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.baseExperience();
    }
    
    @Override
    protected Enum<QuesConfigModel.Choices> increaseMethod() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.multiplicativeOrAdditive();
    }
    
    @Override
    protected Double amountToIncrease() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.enchantingOptions.amount();
    }
}
