package qsided.rpmechanics.gui.skills;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.config.QuesConfigModel;

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
    
    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    private static int costReduction(Integer level) {
        if (isBetween(level, 33, 65)) {
            return 1;
        } else if (isBetween(level, 66, 100)) {
            return 2;
        }
        return 0;
    }
    private static int modifier (Integer level) {
        if (isBetween(level, 20, 39)) {
            return 1;
        }
        else if (isBetween(level, 40, 59)) {
            return 2;
        }
        else if (isBetween(level, 60, 79)) {
            return 3;
        }
        else if (isBetween(level, 80, 99)) {
            return 4;
        }
        else if (level >= 100) {
            return 5;
        }
        return 0;
    }
    
    @Override
    protected int milestoneCount() {
        return 1;
    }
    
    @Override
    protected List<Milestone> milestones() {
        return List.of();
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
