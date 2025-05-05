package qsided.rpmechanics.config.roleplay_classes;

public class StartingSkill {
    
    String skill;
    Integer level;
    
    public StartingSkill() {
        super();
    }
    
    public StartingSkill(String skill, Integer level) {
        this.skill = skill;
        this.level = level;
    }
    
    public String getSkill() {
        return skill;
    }
    
    public void setSkill(String skill) {
        this.skill = skill;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
}
