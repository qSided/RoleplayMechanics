package qsided.rpmechanics.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import qsided.rpmechanics.blocks.QuesBlocks;

import java.util.concurrent.CompletableFuture;

public class QuesBlockLootTableProvider extends FabricBlockLootTableProvider {
    
    public QuesBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }
    
    @Override
    public void generate() {
        addDrop(QuesBlocks.MYTHRIL_DEBRIS);
        addDrop(QuesBlocks.OVEN);
    }
}
