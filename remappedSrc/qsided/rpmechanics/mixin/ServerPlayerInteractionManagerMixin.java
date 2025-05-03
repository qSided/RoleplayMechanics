package qsided.rpmechanics.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import qsided.rpmechanics.events.PlayerCancelBreakingEvent;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    
    @Shadow protected ServerWorld world;
    
    @Shadow @Final protected ServerPlayerEntity player;
    
    @WrapMethod(method = "processBlockBreakingAction")
    public void onCancelled(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, Operation<Void> original) {
        if (action.equals(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) || action.equals(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK)) {
            PlayerCancelBreakingEvent.EVENT.invoker().cancelBreaking(world, pos, world.getBlockState(pos), player);
            original.call(pos, action, direction, worldHeight, sequence);
        } else {
            original.call(pos, action, direction, worldHeight, sequence);
        }
    }
    
}
