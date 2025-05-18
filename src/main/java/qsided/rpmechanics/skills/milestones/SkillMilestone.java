package qsided.rpmechanics.skills.milestones;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "milestone")
public class SkillMilestone {
    @JacksonXmlProperty(localName = "text")
    private String text;
    @JacksonXmlProperty(localName = "skill")
    private String skill;
    @JacksonXmlProperty(localName = "rewardType")
    private String rewardType;
    @JacksonXmlProperty(localName = "rewardAmount")
    private Integer rewardAmount;
    @JacksonXmlProperty(localName = "levelReq")
    private Integer levelReq;
    
    public SkillMilestone() {
    }
    
    public SkillMilestone(String text, String skill, String rewardType, Integer rewardAmount, Integer levelReq) {
        this.text = text;
        this.skill = skill;
        this.rewardType = rewardType;
        this.rewardAmount = rewardAmount;
        this.levelReq = levelReq;
    }
    
    public enum Type {
        ENCHANT_COST_REDUCTION("enchant_cost_reduction"),
        ENCHANT_POWER("plus_enchant_option"),
        ITEM_REWARD("item"),
        SKILL_XP_REWARD("skill_exp"),
        GENERIC("generic");
        
        Type(String e) {
        }
    }
    
    public String getRewardType() {
        return rewardType;
    }
    
    public boolean hasMet(Integer skillLevel) {
        return skillLevel >= getLevelReq();
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getSkill() {
        return skill;
    }
    
    public void setSkill(String skill) {
        this.skill = skill;
    }
    
    public Integer getLevelReq() {
        return levelReq;
    }
    
    public void setLevelReq(Integer levelReq) {
        this.levelReq = levelReq;
    }
    
    public Integer getRewardAmount() {
        return rewardAmount;
    }
    
    public void setRewardAmount(Integer rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}
