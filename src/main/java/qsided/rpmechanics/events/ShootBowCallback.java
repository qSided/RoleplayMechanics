package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ShootBowCallback {
    
    Event<ShootBowCallback> EVENT = EventFactory.createArrayBacked(ShootBowCallback.class,
            (listeners) -> (world, player, hand) -> {
                for (ShootBowCallback listener : listeners) {
                    ActionResult result = listener.shoot(world, player, hand);
                    
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult shoot(World world, PlayerEntity player, Hand hand);
}
