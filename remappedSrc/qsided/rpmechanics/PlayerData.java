package qsided.rpmechanics;

import java.util.HashMap;

public class PlayerData {
    
    public HashMap<String, Integer> skillLevels = new HashMap<>();
    public HashMap<String, Float> skillExperience = new HashMap<>();
    
    public HashMap<String, HashMap<String, Integer>> expModifiers = new HashMap<>();
    
    public String rpClass = "";
    public Integer rpClassLevel = 1;
    public Float rpClassExp = 0F;
    public Boolean hasJoinedBefore = false;
}
