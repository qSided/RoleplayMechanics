package qsided.rpmechanics.config.roleplay_classes;

import java.util.List;

public class RoleplayClass {
    
    String name;
    String color;
    String description;
    List<StartingEquipment> startingEquipment;
    List<StartingSkill> startingSkills;
    List<SkillModifier> skillModifiers;
    
    public RoleplayClass() {
        super();
    }
    
    public RoleplayClass(String name, String color, String description, List<StartingEquipment> startingEquipment, List<StartingSkill> startingSkills, List<SkillModifier> skillModifiers) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.startingEquipment = startingEquipment;
        this.startingSkills = startingSkills;
        this.skillModifiers = skillModifiers;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<StartingEquipment> getStartingEquipment() {
        return startingEquipment;
    }
    
    public void setStartingEquipment(List<StartingEquipment> startingEquipment) {
        this.startingEquipment = startingEquipment;
    }
    
    public List<StartingSkill> getStartingSkills() {
        return startingSkills;
    }
    
    public void setStartingSkills(List<StartingSkill> startingSkills) {
        this.startingSkills = startingSkills;
    }
    
    public List<SkillModifier> getSkillModifiers() {
        return skillModifiers;
    }
    
    public void setSkillModifiers(List<SkillModifier> skillModifiers) {
        this.skillModifiers = skillModifiers;
    }
}
