package qsided.rpmechanics.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import qsided.rpmechanics.blocks.QuesBlocks;

public class OvenRecipe extends AbstractCookingRecipe {
    
    public OvenRecipe(String group, CookingRecipeCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(group, category, ingredient, result, experience, cookingTime);
    }
    
    @Override
    public RecipeSerializer<OvenRecipe> getSerializer() {
        return QuesRecipeSerializers.OVEN_RECIPE_SERIALIZER;
    }
    
    @Override
    public RecipeType<OvenRecipe> getType() {
        return QuesRecipeTypes.OVEN_RECIPE_TYPE;
    }
    
    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.SMOKER_FOOD;
    }
    
    @Override
    protected Item getCookerItem() {
        return QuesBlocks.OVEN.asItem();
    }
}
