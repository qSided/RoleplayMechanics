package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import qsided.rpmechanics.PlayerData;

public interface IncreaseSkillLevelCallback {
    
    Event<IncreaseSkillLevelCallback> EVENT = EventFactory.createArrayBacked(IncreaseSkillLevelCallback.class,
            (listeners) -> (player, state, skill, value, shouldMessage) -> {
                for (IncreaseSkillLevelCallback listener : listeners) {
                    ActionResult result = listener.increaseLevel(player, state, skill, value, shouldMessage);
                    
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult increaseLevel(ServerPlayerEntity player, PlayerData state, String skill, Integer value, boolean shouldMessage);
}
