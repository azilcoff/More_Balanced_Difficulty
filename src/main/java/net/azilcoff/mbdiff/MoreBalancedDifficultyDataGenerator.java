package net.azilcoff.mbdiff;

import net.azilcoff.datagen.ModChestLootTableProvider;
import net.azilcoff.datagen.ModEntityLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MoreBalancedDifficultyDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModEntityLootTableProvider::new);
        pack.addProvider(ModChestLootTableProvider::new);
	}
}
