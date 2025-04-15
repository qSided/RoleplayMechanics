package qsided.rpmechanics.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import qsided.rpmechanics.blockentities.OvenBlockEntity;
import qsided.rpmechanics.blockentities.QuesBlockEntityTypes;

public class OvenBlock extends BlockWithEntity {
    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    
    protected OvenBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIT, false));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
    
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OvenBlockEntity(pos, state);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, QuesBlockEntityTypes.OVEN_BLOCK, OvenBlockEntity::tick);
    }
    
    @Override
    protected MapCodec<? extends OvenBlock> getCodec() {
        return createCodec(OvenBlock::new);
    }
    
    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof OvenBlockEntity ovenBlockEntity)) {
            return super.onUse(state, world, pos, player, hit);
        }
        NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            
        if (screenHandlerFactory != null) {
            player.openHandledScreen(screenHandlerFactory);
        }
        return ActionResult.SUCCESS;
    }
    
    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof OvenBlockEntity ovenBlockEntity) {
                ItemScatterer.spawn(world, pos, ovenBlockEntity);
                world.updateComparators(pos, this);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
    
    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }
    
    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }
}
