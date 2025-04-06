package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlayerCancelBreakingCallback {
    
    Event<PlayerCancelBreakingCallback> EVENT = EventFactory.createArrayBacked(PlayerCancelBreakingCallback.class,
            (listeners) -> (world, pos, blockState, player) -> {
                for (PlayerCancelBreakingCallback listener : listeners) {
                    ActionResult result = listener.cancelBreaking(world, pos, blockState, player);
                    
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult cancelBreaking(World world, BlockPos pos, BlockState blockState, PlayerEntity player);
}
