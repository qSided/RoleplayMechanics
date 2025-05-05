package qsided.rpmechanics.gui.skills;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.config.QuesConfigModel;
import qsided.rpmechanics.items.QuesItems;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WoodcuttingScreen extends SkillScreen {
    
    public static Integer level;
    public static Float exp;
    
    public static Float getExp() {
        return exp;
    }
    
    public static void setExp(Float exp) {
        WoodcuttingScreen.exp = exp;
    }
    
    public static Integer getLevel() {
        return level;
    }
    
    public static void setLevel(Integer level) {
        WoodcuttingScreen.level = level;
    }
    
    @Override
    protected MutableText skillDescription() {
        return Text.translatable("skills.descriptions.rpmechanics.woodcutting");
    }
    
    @Override
    protected int skillDescriptionOffset() {
        return 6;
    }
    
    @Override
    protected ItemStack skillIcon() {
        return Items.OAK_LOG.getDefaultStack();
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
                Text.translatable("skills.rpmechanics.woodcutting.efficiency"),
                Text.literal("+" + df.format((skillLevel()-1) * RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.woodcuttingSettings.choppingSpeed()) + "%")
        ));
        stats.add(new SkillStatistic(
                Text.translatable("skills.rpmechanics.woodcutting.extra_chance"),
                Text.literal("+" + (skillLevel()-1) + "%")
        ));
        return stats;
    }
    
    @Override
    protected int milestoneCount() {
        return 1;
    }
    
    @Override
    protected List<Milestone> milestones() {
        List<Milestone> milestones = new ArrayList<>();
        
        milestones.add(new Milestone(RoleplayMechanicsCommon.OWO_CONFIG.skillOptions.woodcuttingSettings.levelForTreeChopping(), Text.translatable("skills.rpmechanics.milestones.treechopping")));
        
        return milestones;
    }
    
    @Override
    protected Float baseExpReq() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.baseExperience();
    }
    
    @Override
    protected Enum<QuesConfigModel.Choices> increaseMethod() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.multiplicativeOrAdditive();
    }
    
    @Override
    protected Double amountToIncrease() {
        return RoleplayMechanicsCommon.OWO_CONFIG.experienceOptions.woodcuttingOptions.amount();
    }
    
    
}
