package qsided.rpmechanics.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class QuesItems {
    
    public static final Item X = register(
            new Item(new Item.Settings()),
            "x"
    );public static final Item CHECKMARK = register(
            new Item(new Item.Settings()),
            "checkmark"
    );
    public static final Item EXPERIENCE = register(
            new Item(new Item.Settings()),
            "experience"
    );
    public static final Item EXPERIENCE_DISABLED = register(
            new Item(new Item.Settings()),
            "experience_disabled"
    );
    
    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(RoleplayMechanicsCommon.MOD_ID, id);
        
        // Register the item.
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
        
        // Return the registered item!
        return registeredItem;
    }
    
    public static void initialize() {}
}
