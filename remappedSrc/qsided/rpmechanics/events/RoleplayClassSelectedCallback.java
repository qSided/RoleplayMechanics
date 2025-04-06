package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import qsided.rpmechanics.PlayerData;

public interface RoleplayClassSelectedCallback {
    
    Event<RoleplayClassSelectedCallback> EVENT = EventFactory.createArrayBacked(RoleplayClassSelectedCallback.class,
            (listeners) -> (player, state, rpClassId) -> {
                for (RoleplayClassSelectedCallback listener : listeners) {
                    ActionResult result = listener.selectClass(player, state, rpClassId);
                    
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult selectClass(ServerPlayerEntity player, PlayerData state, Integer rpClassId);
}
