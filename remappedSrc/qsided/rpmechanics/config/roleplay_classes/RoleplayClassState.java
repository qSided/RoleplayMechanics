package qsided.rpmechanics.config.roleplay_classes;

public class RoleplayClassState {
    Integer rpClassId = 2124891821;
    Integer rpClassLevel = 1;
    Float rpClassExperience = 0F;
    
    public RoleplayClassState() {
    }
    
    public RoleplayClassState(Integer rpClassId, Integer rpClassLevel, Float rpClassExperience) {
        this.rpClassId = rpClassId;
        this.rpClassLevel = rpClassLevel;
        this.rpClassExperience = rpClassExperience;
    }
    
    public Integer getRpClassId() {
        return rpClassId;
    }
    
    public void setRpClassId(Integer rpClassId) {
        this.rpClassId = rpClassId;
    }
    
    public Integer getRpClassLevel() {
        return rpClassLevel;
    }
    
    public void setRpClassLevel(Integer rpClassLevel) {
        this.rpClassLevel = rpClassLevel;
    }
    
    public Float getRpClassExperience() {
        return rpClassExperience;
    }
    
    public void setRpClassExperience(Float rpClassExperience) {
        this.rpClassExperience = rpClassExperience;
    }
}
