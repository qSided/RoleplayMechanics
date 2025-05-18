package qsided.rpmechanics.blocks;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.gui.OvenScreenHandler;
import qsided.rpmechanics.gui.SkillEnabledEnchantingTableScreenHandler;

import java.util.function.ToIntFunction;

public class QuesBlocks {
    public static final Block OVEN = register(
            new OvenBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.5F)
                    .luminance(createLightLevelFromLitBlockState(13))),
            "oven",
            true);
    public static final Block ENCHANTING_TABLE = register(
            new SkillEnabledEnchantingTable(
                    AbstractBlock.Settings.create().mapColor(MapColor.RED).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().luminance(state -> 7).strength(5.0F, 1200.0F)
            ),
            "se_enchanting_table",
            true
    );
    
    public static final ScreenHandlerType<OvenScreenHandler> OVEN_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(RoleplayMechanicsCommon.MOD_ID, "oven_block"), new ScreenHandlerType<>(OvenScreenHandler::new, FeatureSet.empty()));
    public static final ScreenHandlerType<SkillEnabledEnchantingTableScreenHandler> SE_ENCHANTING_TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(RoleplayMechanicsCommon.MOD_ID, "skill_enabled_enchanting_table"), new ScreenHandlerType<>(SkillEnabledEnchantingTableScreenHandler::new, FeatureSet.empty()));
    
    public static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }
    
    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        Identifier id = Identifier.of(RoleplayMechanicsCommon.MOD_ID, name);
        
        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }
        
        return Registry.register(Registries.BLOCK, id, block);
    }
    
    public static void initialize() {
    }
}
