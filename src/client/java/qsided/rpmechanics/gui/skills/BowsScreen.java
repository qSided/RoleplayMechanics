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

public class BowsScreen extends SkillScreen {
    
    public static Integer level;
    public static Float exp;
    
    public static Float getExp() {
        return exp;
    }
    
    public static void setExp(Float exp) {
        BowsScreen.exp = exp;
    }
    
    public static Integer getLevel() {
        return level;
    }
    
    public static void setLevel(Integer level) {
        BowsScreen.level = level;
    }
    
    @Override
    protected MutableText skillDescription() {
        return Text.translatable("skills.descriptions.rpmechanics.bows");
    }
    
    @Override
    protected int skillDescriptionOffset() {
        return 6;
    }
    
    @Override
    protected ItemStack skillIcon() {
        return Items.BOW.getDefaultStack();
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
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.bows.arrow_accuracy"),
                Text.literal("+" + (df.format((skillLevel()-1) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.bowsSettings.accuracy())) + "%")
        ));
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.bows.arrow_speed"),
                Text.literal("+" + df.format((skillLevel()-1) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.bowsSettings.speed()) + "%")
        ));
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
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.baseExperience();
    }
    
    @Override
    protected Enum<QuesConfigModel.Choices> increaseMethod() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.multiplicativeOrAdditive();
    }
    
    @Override
    protected Double amountToIncrease() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.bowsOptions.amount();
    }
    
    
}
