package qsided.rpmechanics.gui.skills;

import net.minecraft.text.MutableText;

public class SkillStatistic {
    
    public MutableText statTranslationKey;
    public MutableText statValueAsText;
    
    public SkillStatistic() {}
    
    public SkillStatistic(MutableText statName, MutableText statValue) {
        this.statTranslationKey = statName;
        this.statValueAsText = statValue;
    }
    
    public MutableText getStatTranslationKey() {
        return statTranslationKey;
    }
    
    public void setStatTranslationKey(MutableText statTranslationKey) {
        this.statTranslationKey = statTranslationKey;
    }
    
    public MutableText getStatValueAsText() {
        return statValueAsText;
    }
    
    public void setStatValueAsText(MutableText statValueAsText) {
        this.statValueAsText = statValueAsText;
    }
}
