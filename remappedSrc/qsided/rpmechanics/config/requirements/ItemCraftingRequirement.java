package qsided.rpmechanics.config.requirements;

public class ItemCraftingRequirement {
    public String itemId;
    public Integer levelReq;
    public Float expWorth;
    
    public ItemCraftingRequirement() {
    }
    
    public ItemCraftingRequirement(String id, Integer levelReq, Float expWorth) {
        this.itemId = id;
        this.levelReq = levelReq;
        this.expWorth = expWorth;
    }
    
    public Integer getLevelReq() {
        return levelReq;
    }
    
    public void setLevelReq(Integer levelReq) {
        this.levelReq = levelReq;
    }
    
    public String getItemId() {
        return itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public Float getExpWorth() {
        return expWorth;
    }
    
    public void setExpWorth(Float expWorth) {
        this.expWorth = expWorth;
    }
}
