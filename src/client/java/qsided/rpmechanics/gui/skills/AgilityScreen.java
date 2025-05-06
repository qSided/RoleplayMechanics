package qsided.rpmechanics.gui.skills;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.config.QuesConfigModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AgilityScreen extends SkillScreen {
    
    public static Integer level;
    public static Float exp;
    public static Double safeDistance;
    
    public static Double getSafeDistance() {
        return safeDistance;
    }
    
    public static void setSafeDistance(Double safeDistance) {
        AgilityScreen.safeDistance = safeDistance;
    }
    
    public static Float getExp() {
        return exp;
    }
    
    public static void setExp(Float exp) {
        AgilityScreen.exp = exp;
    }
    
    public static Integer getLevel() {
        return level;
    }
    
    public static void setLevel(Integer level) {
        AgilityScreen.level = level;
    }
    
    @Override
    protected MutableText skillDescription() {
        return Text.translatable("skills.descriptions.rpmechanics.agility");
    }
    
    @Override
    protected int skillDescriptionOffset() {
        return 0;
    }
    
    @Override
    protected ItemStack skillIcon() {
        return Items.LEATHER_BOOTS.getDefaultStack();
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
        return 5;
    }
    
    @Override
    protected List<SkillStatistic> information() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        List<SkillStatistic> stats = new ArrayList<>();
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.agility.movement_speed"),
                Text.literal("+" + (df.format(calculatePercentageDifference(0.7 + (skillLevel()-1) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.agilitySettings.movementSpeed(), 0.7))) + "%")));
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.agility.jump_strength"),
                Text.literal("+" + (df.format(calculatePercentageDifference(0.42 + (skillLevel()-1) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.agilitySettings.jumpStrength(), 0.42))) + "%")));
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.agility.safe_distance"),
                Text.literal(df.format(getSafeDistance()) + " Blocks")));
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
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.baseExperience();
    }
    
    @Override
    protected Enum<QuesConfigModel.Choices> increaseMethod() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.multiplicativeOrAdditive();
    }
    
    @Override
    protected Double amountToIncrease() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.agilityOptions.amount();
    }
}
