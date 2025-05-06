package qsided.rpmechanics.items;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class QuesComponents {
    
    public static final ComponentType<String> COOK_QUALITY = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(RoleplayMechanicsCommon.MOD_ID, "cook_quality"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );
    
    public static void initialize() {
    
    }
}
