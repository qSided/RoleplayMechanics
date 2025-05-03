package qsided.rpmechanics.config.requirements;

public class ItemWithRequirements {
    String itemId;
    Requirements requirements;
    
    public ItemWithRequirements() {
        super();
    }
    
    public ItemWithRequirements(String itemId, Requirements requirements) {
        this.itemId = itemId;
        this.requirements = requirements;
    }
    
    public String getItemId() {
        return itemId;
    }
    
    public Requirements getRequirements() {
        return requirements;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }
}
