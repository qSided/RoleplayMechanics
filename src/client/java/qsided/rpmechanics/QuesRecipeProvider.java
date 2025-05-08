package qsided.rpmechanics;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import qsided.rpmechanics.blocks.QuesBlocks;

import java.util.concurrent.CompletableFuture;

public class QuesRecipeProvider extends FabricRecipeProvider {
    
    public QuesRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    public void generate(RecipeExporter recipeExporter) {
        
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, QuesBlocks.OVEN, 1)
                .pattern(" w ")
                .pattern("ghg")
                .pattern(" j ")
                .input('w', Items.REDSTONE)
                .input('g', ItemTags.LOGS)
                .input('h', Items.SMOKER)
                .input('j', Items.IRON_INGOT)
                .criterion(hasItem(Items.SMOKER), conditionsFromItem(QuesBlocks.OVEN))
                .offerTo(recipeExporter);
    }
}
