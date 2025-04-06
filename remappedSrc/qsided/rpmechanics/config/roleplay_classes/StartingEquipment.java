package qsided.rpmechanics.config.roleplay_classes;



public class StartingEquipment {
    
    String item;
    Integer count;
    
    public StartingEquipment() {
        super();
    }
    
    public StartingEquipment(String item, Integer count) {
        this.item = item;
        this.count = count;
    }
    
    public String getItem() {
        return item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
}
