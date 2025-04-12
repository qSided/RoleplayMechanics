package qsided.rpmechanics.blockentities;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.blocks.QuesBlocks;

public class RoleplayMechanicsBlockEntityTypes {
    
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(RoleplayMechanicsCommon.MOD_ID, path), blockEntityType);
    }
    
    //public static final BlockEntityType<OvenBlockEntity> OVEN_BLOCK = register(
    //        "oven_block",
    //        FabricBlockEntityTypeBuilder.create(OvenBlockEntity::new, QuesBlocks.OVEN).build()
    //);
    
    public static void initialize() {
    }

}
