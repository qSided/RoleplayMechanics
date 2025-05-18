package qsided.rpmechanics.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Nameable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.blockentities.QuesBlockEntityTypes;
import qsided.rpmechanics.blockentities.SkillEnabledEnchantingTableBlockEntity;
import qsided.rpmechanics.gui.SkillEnabledEnchantingTableScreenHandler;
import qsided.rpmechanics.networking.SendPlayerS2CPayload;

import java.util.List;

public class SkillEnabledEnchantingTable extends BlockWithEntity {
    
    public static final MapCodec<SkillEnabledEnchantingTable> CODEC = createCodec(SkillEnabledEnchantingTable::new);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    public static final List<BlockPos> POWER_PROVIDER_OFFSETS = BlockPos.stream(-2, 0, -2, 2, 1, 2)
            .filter(pos -> Math.abs(pos.getX()) == 2 || Math.abs(pos.getZ()) == 2)
            .map(BlockPos::toImmutable)
            .toList();
    
    @Override
    public MapCodec<SkillEnabledEnchantingTable> getCodec() {
        return CODEC;
    }
    
    public SkillEnabledEnchantingTable(AbstractBlock.Settings settings) {
        super(settings);
    }
    
    public static boolean canAccessPowerProvider(World world, BlockPos tablePos, BlockPos providerOffset) {
        return world.getBlockState(tablePos.add(providerOffset)).isIn(BlockTags.ENCHANTMENT_POWER_PROVIDER)
                && world.getBlockState(tablePos.add(providerOffset.getX() / 2, providerOffset.getY(), providerOffset.getZ() / 2))
                .isIn(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    }
    
    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return true;
    }
    
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        
        for (BlockPos blockPos : POWER_PROVIDER_OFFSETS) {
            if (random.nextInt(16) == 0 && canAccessPowerProvider(world, pos, blockPos)) {
                world.addParticle(
                        ParticleTypes.ENCHANT,
                        pos.getX() + 0.5,
                        pos.getY() + 2.0,
                        pos.getZ() + 0.5,
                        blockPos.getX() + random.nextFloat() - 0.5,
                        blockPos.getY() - random.nextFloat() - 1.0F,
                        blockPos.getZ() + random.nextFloat() - 0.5
                );
            }
        }
    }
    
    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SkillEnabledEnchantingTableBlockEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, QuesBlockEntityTypes.ENCHANTING_TABLE_BLOCK, SkillEnabledEnchantingTableBlockEntity::tick);
    }
    
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            PlayerData pState = StateManager.getPlayerState(player);
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            ServerPlayNetworking.send((ServerPlayerEntity) player, new SendPlayerS2CPayload(pState.skillLevels.getOrDefault("enchanting", 1)));
            return ActionResult.CONSUME;
        }
    }
    
    @Nullable
    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SkillEnabledEnchantingTableBlockEntity) {
            Text text = ((Nameable)blockEntity).getDisplayName();
            return new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, player) -> new SkillEnabledEnchantingTableScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), text
            );
        } else {
            return null;
        }
    }
    
    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
