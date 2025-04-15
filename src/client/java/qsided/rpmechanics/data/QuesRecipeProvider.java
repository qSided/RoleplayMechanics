package qsided.rpmechanics.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.*;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.items.QuesItems;
import qsided.rpmechanics.recipes.OvenCookingRecipeJsonBuilder;
import qsided.rpmechanics.recipes.OvenRecipe;
import qsided.rpmechanics.recipes.QuesRecipeSerializers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QuesRecipeProvider extends FabricRecipeProvider {
    
    
    
    public QuesRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
        
    }
    
    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
                //Mythril Upgrade Smithing Template Recipe
                createShaped(RecipeCategory.TOOLS, QuesItems.MYTHRIL_UPGRADE_TEMPLATE, 2)
                        .pattern("lwl")
                        .pattern("lxl")
                        .pattern("lll")
                        .input('l', Items.NETHERITE_INGOT)
                        .input('w', QuesItems.MYTHRIL_UPGRADE_TEMPLATE)
                        .input('x', Items.END_STONE)
                        .group("mythril_upgrade")
                        .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(QuesItems.MYTHRIL_UPGRADE_TEMPLATE))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.MISC, QuesItems.MYTHRIL_INGOT, 1)
                        .input(QuesItems.MYTHRIL_FRAGMENT, 4)
                        .input(Items.COPPER_INGOT, 4)
                        .group("mythril_ingot")
                        .criterion(hasItem(QuesItems.MYTHRIL_FRAGMENT), conditionsFromItem(QuesItems.MYTHRIL_INGOT))
                        .offerTo(exporter);
                
                //Mythril Debris to Fragment Stonecutter Recipe
                offerStonecuttingRecipe(RecipeCategory.MISC, QuesItems.MYTHRIL_FRAGMENT, QuesBlocks.MYTHRIL_DEBRIS.asItem(), 2);
                //Ancient Debris to Netherite Scrap Stonecutter Recipe
                offerStonecuttingRecipe(RecipeCategory.MISC, Items.NETHERITE_SCRAP, Blocks.ANCIENT_DEBRIS.asItem(), 2);
                
                offerMythrilUpgradeRecipe(Items.NETHERITE_HELMET, RecipeCategory.COMBAT, QuesItems.MYTHRIL_HELMET);
                offerMythrilUpgradeRecipe(Items.NETHERITE_CHESTPLATE, RecipeCategory.COMBAT, QuesItems.MYTHRIL_CHESTPLATE);
                offerMythrilUpgradeRecipe(Items.NETHERITE_LEGGINGS, RecipeCategory.COMBAT, QuesItems.MYTHRIL_LEGGINGS);
                offerMythrilUpgradeRecipe(Items.NETHERITE_BOOTS, RecipeCategory.COMBAT, QuesItems.MYTHRIL_BOOTS);
                offerMythrilUpgradeRecipe(Items.NETHERITE_SWORD, RecipeCategory.COMBAT, QuesItems.MYTHRIL_SWORD);
                offerMythrilUpgradeRecipe(Items.NETHERITE_PICKAXE, RecipeCategory.TOOLS, QuesItems.MYTHRIL_PICKAXE);
                offerMythrilUpgradeRecipe(Items.NETHERITE_AXE, RecipeCategory.COMBAT, QuesItems.MYTHRIL_AXE);
                offerMythrilUpgradeRecipe(Items.NETHERITE_SHOVEL, RecipeCategory.TOOLS, QuesItems.MYTHRIL_SHOVEL);
                offerMythrilUpgradeRecipe(Items.NETHERITE_HOE, RecipeCategory.TOOLS, QuesItems.MYTHRIL_HOE);
                
                offerOvenRecipe(List.of(Items.PORKCHOP), RecipeCategory.FOOD, Items.COOKED_PORKCHOP, 8, 120, "oven");
                offerOvenRecipe(List.of(Items.BEEF), RecipeCategory.FOOD, Items.COOKED_BEEF, 8, 120, "oven");
                offerOvenRecipe(List.of(Items.CHICKEN), RecipeCategory.FOOD, Items.COOKED_CHICKEN, 8, 120, "oven");
                offerOvenRecipe(List.of(Items.COD), RecipeCategory.FOOD, Items.COOKED_COD, 8, 120, "oven");
                offerOvenRecipe(List.of(Items.RABBIT), RecipeCategory.FOOD, Items.COOKED_RABBIT, 8, 120, "oven");
                offerOvenRecipe(List.of(Items.MUTTON), RecipeCategory.FOOD, Items.COOKED_MUTTON, 8, 120, "oven");
                offerOvenRecipe(List.of(Items.POTATO), RecipeCategory.FOOD, Items.BAKED_POTATO, 8, 120, "oven");
            }
            
            public void offerMythrilUpgradeRecipe(Item input, RecipeCategory category, Item result) {
                SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItem(QuesItems.MYTHRIL_UPGRADE_TEMPLATE), Ingredient.ofItem(input), Ingredient.ofItem(QuesItems.MYTHRIL_INGOT), category, result).criterion("has_netherite_ingot", conditionsFromItem(QuesItems.MYTHRIL_INGOT)).offerTo(exporter, getItemPath(result) + "_smithing");
            }
            
            public void offerOvenRecipe(List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
                this.offerMultiple(OvenRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_cooking");
            }
            
            public <T extends AbstractCookingRecipe> void offerMultiple(
                    AbstractCookingRecipe.RecipeFactory<T> recipeFactory,
                    List<ItemConvertible> inputs,
                    RecipeCategory category,
                    ItemConvertible output,
                    float experience,
                    int cookingTime,
                    String group,
                    String suffix
            ) {
                for (ItemConvertible itemConvertible : inputs) {
                    OvenCookingRecipeJsonBuilder.create(Ingredient.ofItem(itemConvertible), category, output, experience, cookingTime, recipeFactory)
                            .group(group)
                            .criterion(hasItem(itemConvertible), this.conditionsFromItem(itemConvertible))
                            .offerTo(this.exporter, getItemPath(output) + suffix + "_" + getItemPath(itemConvertible));
                }
            }
            
        };
    }
    
    @Override
    public String getName() {
        return "QuesRecipeProvider";
    }
    
    
}
