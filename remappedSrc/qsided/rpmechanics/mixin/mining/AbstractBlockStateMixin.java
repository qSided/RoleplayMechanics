package qsided.rpmechanics.mixin.mining;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import qsided.rpmechanics.events.PlayerStartBreakingEvent;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    
    @WrapMethod(method = "onBlockBreakStart")
    public void onStartBreaking(World world, BlockPos pos, PlayerEntity player, Operation<Void> original) {
        PlayerStartBreakingEvent.EVENT.invoker().startBreaking(world, pos, world.getBlockState(pos), player);
        original.call(world, pos, player);
    }
    
}
