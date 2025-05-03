package qsided.rpmechanics.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlayerStartBreakingEvent {
    
    Event<PlayerStartBreakingEvent> EVENT = EventFactory.createArrayBacked(PlayerStartBreakingEvent.class,
            (listeners) -> (world, pos, blockState, player) -> {
                for (PlayerStartBreakingEvent listener : listeners) {
                    ActionResult result = listener.startBreaking(world, pos, blockState, player);
                    
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                
                return ActionResult.PASS;
            });
    
    ActionResult startBreaking(World world, BlockPos pos, BlockState blockState, PlayerEntity player);
}
