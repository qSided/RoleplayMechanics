package qsided.rpmechanics.recipes;

import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class QuesRecipeTypes {
    
    public static final RecipeType<OvenRecipe> OVEN_RECIPE_TYPE = Registry.register(
            Registries.RECIPE_TYPE,
            Identifier.of(RoleplayMechanicsCommon.MOD_ID, "oven"),
            new RecipeType<OvenRecipe>() {
                @Override
                public String toString() {
                    return "oven";
                }
    });
    
    public static void initialize() {
    
    }
    
}
