package qsided.rpmechanics.config.experience_values;

import java.util.Map;

public class BlockExperience {
    
    public String id;
    public Map<String, Float> experience;
    
    public BlockExperience() {
        super();
    }
    
    public BlockExperience(String id, Map<String, Float> experience) {
        this.id = id;
        this.experience = experience;
    }
    
    public String getId() {
        return id;
    }
    
    public Map<String, Float> getExperience() {
        return experience;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setExperience(Map<String, Float> experience) {
        this.experience = experience;
    }
}
