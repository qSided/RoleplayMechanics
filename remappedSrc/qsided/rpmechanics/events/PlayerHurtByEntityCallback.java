package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface PlayerHurtByEntityCallback {
    
    Event<PlayerHurtByEntityCallback> EVENT = EventFactory.createArrayBacked(PlayerHurtByEntityCallback.class,
            (listeners) -> (player, source, dmgDealt, dmgTaken, dmgBlocked) -> {
                for (PlayerHurtByEntityCallback listener : listeners) {
                    ActionResult result = listener.onDamaged(player, source, dmgDealt, dmgTaken, dmgBlocked);
                    
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult onDamaged(LivingEntity player, DamageSource source, float dmgDealt, float dmgTaken, boolean dmgBlocked);
}
