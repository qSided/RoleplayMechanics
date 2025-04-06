package qsided.rpmechanics;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class QuesModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack quesPack = fabricDataGenerator.createPack();
		
		//quesPack.addProvider(QuesModelProvider::new);
		//quesPack.addProvider(QuesItemTagProvider::new);
		//quesPack.addProvider(QuesBlockTagProvider::new);
		//quesPack.addProvider(QuesRecipeProvider::new);
		//quesPack.addProvider(QuesBlockLootTableProvider::new);
	}
}
