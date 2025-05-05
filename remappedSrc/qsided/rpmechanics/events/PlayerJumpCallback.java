package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public interface PlayerJumpCallback {
    
    Event<PlayerJumpCallback> EVENT = EventFactory.createArrayBacked(PlayerJumpCallback.class,
            (listeners) -> (player, id) -> {
                for (PlayerJumpCallback listener : listeners) {
                    ActionResult result = listener.jump(player, id);
                    
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult jump(LivingEntity player, Identifier id);
}
