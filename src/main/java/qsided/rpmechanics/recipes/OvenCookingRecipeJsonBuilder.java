package qsided.rpmechanics.recipes;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OvenCookingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    
    private final RecipeCategory category;
    private final CookingRecipeCategory cookingCategory;
    private final Item output;
    private final Ingredient input;
    private final float experience;
    private final int cookingTime;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable private String group;
    private final AbstractCookingRecipe.RecipeFactory<?> recipeFactory;
    
    private OvenCookingRecipeJsonBuilder(
            RecipeCategory category,
            CookingRecipeCategory cookingCategory,
            ItemConvertible output,
            Ingredient input,
            float experience,
            int cookingTime,
            AbstractCookingRecipe.RecipeFactory<?> recipeFactory
    ) {
        this.category = category;
        this.cookingCategory = cookingCategory;
        this.output = output.asItem();
        this.input = input;
        this.experience = experience;
        this.cookingTime = cookingTime;
        this.recipeFactory = recipeFactory;
    }
    
    public static <T extends AbstractCookingRecipe> OvenCookingRecipeJsonBuilder create(
            Ingredient input,
            RecipeCategory category,
            ItemConvertible output,
            float experience,
            int cookingTime,
            AbstractCookingRecipe.RecipeFactory<T> recipeFactory
    ) {
        return new OvenCookingRecipeJsonBuilder(category, CookingRecipeCategory.FOOD, output, input, experience, cookingTime, recipeFactory);
    }
    
    @Override
    public CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }
    
    @Override
    public CraftingRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }
    
    @Override
    public Item getOutputItem() {
        return this.output;
    }
    
    @Override
    public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
        this.validate(recipeKey);
        Advancement.Builder builder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(builder::criterion);
        AbstractCookingRecipe abstractCookingRecipe = this.recipeFactory
                .create(Objects.requireNonNullElse(this.group, ""), this.cookingCategory, this.input, new ItemStack(this.output), this.experience, this.cookingTime);
        exporter.accept(recipeKey, abstractCookingRecipe, builder.build(recipeKey.getValue().withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }
    
    private void validate(RegistryKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.getValue());
        }
    }
    
    
}
