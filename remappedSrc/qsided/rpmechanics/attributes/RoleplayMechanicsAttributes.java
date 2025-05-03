package qsided.rpmechanics.attributes;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class RoleplayMechanicsAttributes {
    
    public static final RegistryEntry<EntityAttribute> BOW_PROJECTILE_SPEED = Registry.registerReference(
            Registries.ATTRIBUTE,
            Identifier.of(
                    RoleplayMechanicsCommon.MOD_ID,
                    "bow_projectile_speed"), new ClampedEntityAttribute("attribute.rpmechanics.bow_projectile_speed", 0.0, 0.0, 512.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> BOW_PROJECTILE_ACCURACY = Registry.registerReference(
            Registries.ATTRIBUTE,
            Identifier.of(
                    RoleplayMechanicsCommon.MOD_ID,
                    "bow_projectile_accuracy"), new ClampedEntityAttribute("attribute.rpmechanics.bow_projectile_accuracy", 0.0, 0.0, 512.0).setTracked(true));
    
    public static void initialize() {}
    
}
