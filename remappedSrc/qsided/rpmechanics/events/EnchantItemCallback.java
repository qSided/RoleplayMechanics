package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface EnchantItemCallback {
    
    Event<EnchantItemCallback> EVENT = EventFactory.createArrayBacked(EnchantItemCallback.class,
            (listeners) -> (player, stack, levelsSpent) -> {
                for (EnchantItemCallback listener : listeners) {
                    ActionResult result = listener.interact(player, stack, levelsSpent);
                    
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult interact(ServerPlayerEntity player, ItemStack stack, int levelsSpent);
}
