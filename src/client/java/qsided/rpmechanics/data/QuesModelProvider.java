package qsided.rpmechanics.data;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TexturedModel;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.items.QuesItems;

public class QuesModelProvider extends FabricModelProvider {
    public QuesModelProvider(FabricDataOutput output) {
        super(output);
    }
    
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSingleton(QuesBlocks.MYTHRIL_DEBRIS, TexturedModel.END_FOR_TOP_CUBE_COLUMN);
    }
    
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(QuesItems.X, Models.GENERATED);
        itemModelGenerator.register(QuesItems.CHECKMARK, Models.GENERATED);
        
        itemModelGenerator.register(QuesItems.MYTHRIL_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(QuesItems.MYTHRIL_INGOT, Models.GENERATED);
        itemModelGenerator.register(QuesItems.MYTHRIL_UPGRADE_TEMPLATE, Models.GENERATED);
        itemModelGenerator.register(QuesItems.MYTHRIL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(QuesItems.MYTHRIL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(QuesItems.MYTHRIL_AXE, Models.HANDHELD);
        itemModelGenerator.register(QuesItems.MYTHRIL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(QuesItems.MYTHRIL_HOE, Models.HANDHELD);
        
        itemModelGenerator.registerArmor(QuesItems.MYTHRIL_HELMET, RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset")), Identifier.of(RoleplayMechanicsCommon.MOD_ID, "mythril")), "helmet", false);
        itemModelGenerator.registerArmor(QuesItems.MYTHRIL_CHESTPLATE, RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset")), Identifier.of(RoleplayMechanicsCommon.MOD_ID, "mythril")), "chestplate", false);
        itemModelGenerator.registerArmor(QuesItems.MYTHRIL_LEGGINGS, RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset")), Identifier.of(RoleplayMechanicsCommon.MOD_ID, "mythril")), "leggings", false);
        itemModelGenerator.registerArmor(QuesItems.MYTHRIL_BOOTS, RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset")), Identifier.of(RoleplayMechanicsCommon.MOD_ID, "mythril")), "boots", false);
        
        
    }
}
