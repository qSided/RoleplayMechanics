package qsided.rpmechanics.blockentities;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.blocks.QuesBlocks;

public class QuesBlockEntityTypes {
    
    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(RoleplayMechanicsCommon.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
    
    public static final BlockEntityType<OvenBlockEntity> OVEN_BLOCK = register(
            "oven_block",
            OvenBlockEntity::new,
            QuesBlocks.OVEN
    );
    
    public static final BlockEntityType<SkillEnabledEnchantingTableBlockEntity> ENCHANTING_TABLE_BLOCK = register(
            "enchanting_table",
            SkillEnabledEnchantingTableBlockEntity::new,
            QuesBlocks.ENCHANTING_TABLE
    );
    
    public static void initialize() {
    }

}
