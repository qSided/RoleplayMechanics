package qsided.rpmechanics.items.materials;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import qsided.rpmechanics.RoleplayMechanicsCommon;

import java.util.EnumMap;

public class QuesArmorMaterials {
    public static void initialize() {}
    
    public static final ArmorMaterial MYTHRIL = new ArmorMaterial(40, Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, 4);
            map.put(EquipmentType.LEGGINGS, 7);
            map.put(EquipmentType.CHESTPLATE, 9);
            map.put(EquipmentType.HELMET, 4);
            map.put(EquipmentType.BODY, 12);
        }
        ),
                18,
                SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                3.0F,
                0.15F,
                null,
                RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset")), Identifier.of(RoleplayMechanicsCommon.MOD_ID, "mythril")));
}
