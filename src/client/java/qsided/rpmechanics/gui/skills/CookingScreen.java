package qsided.rpmechanics.gui.skills;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.config.QuesConfigModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CookingScreen extends SkillScreen {
    
    public static Integer level;
    public static Float exp;
    
    public static Float getExp() {
        return exp;
    }
    
    public static void setExp(Float exp) {
        CookingScreen.exp = exp;
    }
    
    public static Integer getLevel() {
        return level;
    }
    
    public static void setLevel(Integer level) {
        CookingScreen.level = level;
    }
    
    @Override
    protected MutableText skillDescription() {
        return Text.translatable("skills.descriptions.rpmechanics.cooking");
    }
    
    @Override
    protected int skillDescriptionOffset() {
        return 2;
    }
    
    @Override
    protected ItemStack skillIcon() {
        return QuesBlocks.OVEN.asItem().getDefaultStack();
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
        return 6;
    }
    
    @Override
    protected List<SkillStatistic> information() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        List<SkillStatistic> stats = new ArrayList<>();
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.cooking.speed"),
                Text.literal("+" + df.format((skillLevel()-1) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.cookingSettings.speed() * 100) + "%")));
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.cooking.perfect"),
                Text.literal((df.format(skillLevel() * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.cookingSettings.perfectChanceIncrease() * 100)) + "%")));
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.cooking.burnt"),
                Text.literal(df.format(Math.max(0, (RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.cookingSettings.baseBurnChance() * 100) - (skillLevel() * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.cookingSettings.burnChanceDecrease()) * 100)) + "%")));
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.cooking.average"),
                Text.literal(df.format(100 - (Math.max(0, ((RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.cookingSettings.baseBurnChance() * 100) - skillLevel() * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.cookingSettings.burnChanceDecrease() * 100)) + ((skillLevel()) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.cookingSettings.perfectChanceIncrease() * 100))) + "%")));
        return stats;
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
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.cookingOptions.baseExperience();
    }
    
    @Override
    protected Enum<QuesConfigModel.Choices> increaseMethod() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.cookingOptions.multiplicativeOrAdditive();
    }
    
    @Override
    protected Double amountToIncrease() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.cookingOptions.amount();
    }
}
