package qsided.rpmechanics.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import qsided.rpmechanics.blockentities.OvenBlockEntity;
import qsided.rpmechanics.blockentities.RoleplayMechanicsBlockEntityTypes;

public class OvenBlock extends AbstractFurnaceBlock {
    
    protected OvenBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
    
    //@Override
    //public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
    //    return validateTicker(type, RoleplayMechanicsBlockEntityTypes.OVEN_BLOCK, OvenBlockEntity::tick);
    //}
    
    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> getCodec() {
        return null;
    }
    
    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
    
    }
}
