package qsided.rpmechanics.recipes;

import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class QuesRecipeSerializers {
    
    public static final RecipeSerializer<OvenRecipe> OVEN_RECIPE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(RoleplayMechanicsCommon.MOD_ID, "oven"),
            new AbstractCookingRecipe.Serializer<>(OvenRecipe::new, 100)
    );
    
    public static void initialize() {
    
    }
    
}
