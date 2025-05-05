package qsided.rpmechanics.blocks;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.gui.OvenScreenHandler;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public class QuesBlocks {
    public static final Block MYTHRIL_DEBRIS = registerBlock("mythril_debris", PillarBlock::new, AbstractBlock.Settings.create().mapColor(DyeColor.CYAN).requiresTool().strength(35.0F, 1300.0F).sounds(BlockSoundGroup.ANCIENT_DEBRIS), false);
    public static final Block OVEN = registerBlock("oven", OvenBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresTool()
            .strength(3.5F)
            .luminance(createLightLevelFromLitBlockState(13)), true);
    
    public static final ScreenHandlerType<OvenScreenHandler> OVEN_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(RoleplayMechanicsCommon.MOD_ID, "oven_block"), new ScreenHandlerType<>(OvenScreenHandler::new, FeatureSet.empty()));
    
    public static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }
    
    public static Block registerToRegistry(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        Block block = factory.apply(settings.registryKey(key));
        if (shouldRegisterItem) {
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }
        return Registry.register(Registries.BLOCK, key, block);
    }
    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(RoleplayMechanicsCommon.MOD_ID, id));
    }
    private static Block registerKey(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        return registerToRegistry(keyOf(id), factory, settings, shouldRegisterItem);
    }
    
    private static Block registerBlock(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        return registerKey(id, factory, settings, shouldRegisterItem);
    }
    
    public static void initialize() {
    }
}
