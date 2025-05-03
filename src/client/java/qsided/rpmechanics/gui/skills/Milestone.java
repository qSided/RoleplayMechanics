package qsided.rpmechanics.gui.skills;

import net.minecraft.text.MutableText;

public class Milestone {
    
    public Integer requiredLevel;
    public MutableText translationKey;
    
    public Milestone() {
    }
    
    public Milestone(Integer requiredLevel, MutableText translationKey) {
        this.requiredLevel = requiredLevel;
        this.translationKey = translationKey;
    }
    
    public Integer getRequiredLevel() {
        return requiredLevel;
    }
    
    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }
    
    public MutableText getTranslationKey() {
        return translationKey;
    }
    
    public void setTranslationKey(MutableText translationKey) {
        this.translationKey = translationKey;
    }
}
