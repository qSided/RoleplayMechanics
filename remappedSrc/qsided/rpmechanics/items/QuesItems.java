package qsided.rpmechanics.items;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.items.materials.QuesArmorMaterials;
import qsided.rpmechanics.items.materials.QuesToolMaterials;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class QuesItems {
    
    public static final Identifier EMPTY_SLOT_INGOT_TEXTURE = Identifier.ofVanilla("item/empty_slot_ingot");
    public static final Identifier EMPTY_ARMOR_SLOT_HELMET_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_chestplate");
    public static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_leggings");
    public static final Identifier EMPTY_ARMOR_SLOT_BOOTS_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_boots");
    public static final Identifier EMPTY_SLOT_HOE_TEXTURE = Identifier.ofVanilla("item/empty_slot_hoe");
    public static final Identifier EMPTY_SLOT_AXE_TEXTURE = Identifier.ofVanilla("item/empty_slot_axe");
    public static final Identifier EMPTY_SLOT_SWORD_TEXTURE = Identifier.ofVanilla("item/empty_slot_sword");
    public static final Identifier EMPTY_SLOT_SHOVEL_TEXTURE = Identifier.ofVanilla("item/empty_slot_shovel");
    public static final Identifier EMPTY_SLOT_PICKAXE_TEXTURE = Identifier.ofVanilla("item/empty_slot_pickaxe");
    
    
    
    //public static final RegistryKey<ItemGroup> QUES_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(QuesMod.MOD_ID, "ques_items"));
    //public static final ItemGroup QUES_ITEM_GROUP = FabricItemGroup.builder()
    //        .icon(() -> new ItemStack(QuesItems.MYTHRIL_SWORD))
    //        .displayName(Text.translatable("itemGroup.ques_items"))
    //        .build();
    
    public static final Item MYTHRIL_DEBRIS = registerBlockItem(QuesBlocks.MYTHRIL_DEBRIS, new Item.Settings().fireproof().rarity(Rarity.UNCOMMON));
    public static final Item MYTHRIL_FRAGMENT = registerItem("mythril_fragment", settings -> new Item(settings.maxCount(64).fireproof()));
    public static final Item MYTHRIL_INGOT = registerItem("mythril_ingot", settings -> new Item(settings.fireproof().maxCount(64)));
    public static final Item MYTHRIL_SWORD = registerItem("mythril_sword", settings -> new SwordItem(QuesToolMaterials.MYTHRIL, 3.0F, -2.2F, settings.fireproof()));
    public static final Item MYTHRIL_PICKAXE = registerItem("mythril_pickaxe", settings -> new PickaxeItem(QuesToolMaterials.MYTHRIL, 1.0F, -2.6F, settings.fireproof()));
    public static final Item MYTHRIL_AXE = registerItem("mythril_axe", settings -> new AxeItem(QuesToolMaterials.MYTHRIL, 5.0F, -2.8F, settings.fireproof()));
    public static final Item MYTHRIL_SHOVEL = registerItem("mythril_shovel", settings -> new ShovelItem(QuesToolMaterials.MYTHRIL, 1.5F, -2.8F, settings.fireproof()));
    public static final Item MYTHRIL_HOE = registerItem("mythril_hoe", settings -> new HoeItem(QuesToolMaterials.MYTHRIL, -4.0F, 0.0F, settings.fireproof()));
    public static final Item MYTHRIL_HELMET = registerItem("mythril_helmet", settings -> new ArmorItem(QuesArmorMaterials.MYTHRIL, EquipmentType.HELMET, settings.fireproof()));
    public static final Item MYTHRIL_CHESTPLATE = registerItem("mythril_chestplate", settings -> new ArmorItem(QuesArmorMaterials.MYTHRIL, EquipmentType.CHESTPLATE, settings.fireproof()));
    public static final Item MYTHRIL_LEGGINGS = registerItem("mythril_leggings", settings -> new ArmorItem(QuesArmorMaterials.MYTHRIL, EquipmentType.LEGGINGS, settings.fireproof()));
    public static final Item MYTHRIL_BOOTS = registerItem("mythril_boots", settings -> new ArmorItem(QuesArmorMaterials.MYTHRIL, EquipmentType.BOOTS, settings.fireproof()));
    public static final Item MYTHRIL_UPGRADE_TEMPLATE = registerItem("mythril_upgrade_template", settings -> new SmithingTemplateItem(
            Text.translatable("item.ques-mod.mythril_template_apply").formatted(Formatting.BLUE),
            Text.translatable("item.ques-mod.mythril_template_ingredient").formatted(Formatting.BLUE),
            Text.translatable("item.ques-mod.mythril_template_base_description"),
            Text.translatable("item.ques-mod.mythril_template_addition_description"),
            List.of(
                    EMPTY_ARMOR_SLOT_HELMET_TEXTURE,
                    EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE,
                    EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE,
                    EMPTY_ARMOR_SLOT_BOOTS_TEXTURE,
                    EMPTY_SLOT_HOE_TEXTURE,
                    EMPTY_SLOT_AXE_TEXTURE,
                    EMPTY_SLOT_SWORD_TEXTURE,
                    EMPTY_SLOT_SHOVEL_TEXTURE,
                    EMPTY_SLOT_PICKAXE_TEXTURE),
            List.of(
                    EMPTY_SLOT_INGOT_TEXTURE),
            settings.fireproof().rarity(Rarity.RARE)));
    
    public static Item registerBlockItem(Block block, Item.Settings settings) {
        return registerBlockItemWIthKey(block, BlockItem::new, settings);
    }
    
    public static Item registerBlockItemWIthKey(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        return registerWithKey(blockKeyOf(block.getRegistryEntry().registryKey()), (itemSettings) -> {
            return (Item)factory.apply(block, itemSettings);
        }, settings.useBlockPrefixedTranslationKey());
    }
    
    public static Item registerItem(String id, Function<Item.Settings, Item> factory) {
        return registerWithKey(itemKeyOf(id), factory, new Item.Settings());
    }
    public static RegistryKey<Item> itemKeyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(RoleplayMechanicsCommon.MOD_ID, id));
    }
    
    private static RegistryKey<Item> blockKeyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }
    
    public static Item registerWithKey(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        
        return Registry.register(Registries.ITEM, key, item);
    }
    
    
    
    public static void initialize() {
        //Registry.register(Registries.ITEM_GROUP, QUES_ITEM_GROUP_KEY, QUES_ITEM_GROUP);
        //ItemGroupEvents.modifyEntriesEvent(QUES_ITEM_GROUP_KEY).register(itemGroup -> {
        //    itemGroup.add(MYTHRIL_FRAGMENT);
        //    itemGroup.add(MYTHRIL_INGOT);
        //    itemGroup.add(QuesBlocks.MYTHRIL_CHUNK.asItem());
        //    itemGroup.add(MYTHRIL_UPGRADE_TEMPLATE);
        //    itemGroup.add(MYTHRIL_SWORD);
        //    itemGroup.add(MYTHRIL_PICKAXE);
        //    itemGroup.add(MYTHRIL_AXE);
        //    itemGroup.add(MYTHRIL_SHOVEL);
        //    itemGroup.add(MYTHRIL_HOE);
        //    itemGroup.add(MYTHRIL_HELMET);
        //    itemGroup.add(MYTHRIL_CHESTPLATE);
        //    itemGroup.add(MYTHRIL_LEGGINGS);
        //    itemGroup.add(MYTHRIL_BOOTS);
        //});
    }
}
