package qsided.rpmechanics.recipes;

import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class QuesRecipePropertySets {
    public static final RegistryKey<? extends Registry<RecipePropertySet>> REGISTRY = RegistryKey.ofRegistry(Identifier.of(RoleplayMechanicsCommon.MOD_ID,"recipe_property_set"));
    public static final RegistryKey<RecipePropertySet> OVEN_INPUT = register("oven_input");
    
    private static RegistryKey<RecipePropertySet> register(String id) {
        return RegistryKey.of(REGISTRY, Identifier.of(RoleplayMechanicsCommon.MOD_ID, id));
    }
    
    public static void initialize() {
    
    }
    
}
