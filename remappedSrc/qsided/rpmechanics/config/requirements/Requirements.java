package qsided.rpmechanics.config.requirements;

public class Requirements {
    String skill;
    Integer rpClassId;
    Integer skillLevel;
    
    public Requirements() {
        super();
    }
    
    public Requirements(String skill, Integer rpClassId, Integer level) {
        this.skill = skill;
        this.rpClassId = rpClassId;
        this.skillLevel = level;
    }
    
    public Integer getSkillLevel() {
        return skillLevel;
    }
    
    public String getSkill() {
        return skill;
    }
    
    public Integer getRpClassId() { return rpClassId; }
    
    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }
    
    public void setRpClassId(Integer rpClassId) {
        this.rpClassId = rpClassId;
    }
    
    public void setSkill(String skill) {
        this.skill = skill;
    }
}
