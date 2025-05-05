package qsided.rpmechanics.config.roleplay_classes;

public class SkillModifier {
    
    String skill;
    Integer percentage;
    
    public SkillModifier() {
        super();
    }
    
    public SkillModifier(String skill, Integer percentage) {
        this.skill = skill;
        this.percentage = percentage;
    }
    
    public String getSkill() {
        return skill;
    }
    
    public void setSkill(String skill) {
        this.skill = skill;
    }
    
    public Integer getPercentage() {
        return percentage;
    }
    
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
