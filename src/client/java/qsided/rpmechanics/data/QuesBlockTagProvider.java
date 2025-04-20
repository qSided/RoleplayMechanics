package qsided.rpmechanics.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.tags.blocks.QuesBlockTags;

import java.util.concurrent.CompletableFuture;

public class QuesBlockTagProvider extends FabricTagProvider<Block> {
    
    public QuesBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }
    
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(QuesBlockTags.INCORRECT_FOR_MYTHRIL_TOOL)
                .addOptionalTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL.id())
                .setReplace(true);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(QuesBlocks.MYTHRIL_DEBRIS)
                .add(QuesBlocks.OVEN)
                .setReplace(false);
    }
}
