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

public class FarmingScreen extends SkillScreen {
    
    public static Integer level;
    public static Float exp;
    
    public static Float getExp() {
        return exp;
    }
    
    public static void setExp(Float exp) {
        FarmingScreen.exp = exp;
    }
    
    public static Integer getLevel() {
        return level;
    }
    
    public static void setLevel(Integer level) {
        FarmingScreen.level = level;
    }
    
    @Override
    protected MutableText skillDescription() {
        return Text.translatable("skills.descriptions.rpmechanics.farming");
    }
    
    @Override
    protected int skillDescriptionOffset() {
        return 3;
    }
    
    @Override
    protected ItemStack skillIcon() {
        return Items.WHEAT_SEEDS.getDefaultStack();
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
        return 3;
    }
    
    @Override
    protected List<SkillStatistic> information() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        List<SkillStatistic> stats = new ArrayList<>();
        
        stats.add(new SkillStatistic(Text.translatable("skills.rpmechanics.farming.extra_harvest"), Text.literal("+" + df.format((skillLevel()-1) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.enduranceSettings.health()) + "%")));
        
        return stats;
    }
    
    @Override
    protected int milestoneCount() {
        return 1;
    }
    
    @Override
    protected List<Milestone> milestones() {
        List<Milestone> milestones = new ArrayList<>();
        
        milestones.add(new Milestone(RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.farmingSettings.levelForRightClickHarvest(), Text.translatable("skills.rpmechanics.milestones.rclick_harvest")));
        
        return milestones;
    }
    
    @Override
    protected Float baseExpReq() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.baseExperience();
    }
    
    @Override
    protected Enum<QuesConfigModel.Choices> increaseMethod() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.multiplicativeOrAdditive();
    }
    
    @Override
    protected Double amountToIncrease() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.farmingOptions.amount();
    }
}
