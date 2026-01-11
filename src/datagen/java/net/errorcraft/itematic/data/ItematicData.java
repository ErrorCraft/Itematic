package net.errorcraft.itematic.data;

import net.errorcraft.itematic.data.client.AtlasProvider;
import net.errorcraft.itematic.data.client.ItemBarStyleProvider;
import net.errorcraft.itematic.data.server.RecipeProvider;
import net.errorcraft.itematic.data.server.registry.*;
import net.errorcraft.itematic.data.server.tag.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;

public class ItematicData implements DataGeneratorEntrypoint {
    @Override
    public void addJsonKeySortOrders(JsonKeySortOrderCallback callback) {
        callback.add("id", 0);
        callback.add("event", 1);
        callback.add("handler", 1);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ItemProvider::new);
        pack.addProvider(ArmorMaterialProvider::new);
        pack.addProvider(AtlasProvider::new);
        pack.addProvider(EnchantmentTagProvider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(ItemGroupEntryProviderProvider::new);
        pack.addProvider(ItemGroupEntryProviderTagProvider::new);
        pack.addProvider(TradeProvider::new);
        pack.addProvider(TradeTagProvider::new);
        pack.addProvider(ActionProvider::new);
        pack.addProvider(ActionTagProvider::new);
        pack.addProvider(SmithingTemplateProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(PotionTagProvider::new);
        pack.addProvider(DispenseBehaviorProvider::new);
        pack.addProvider(EntityTypeTagProvider::new);
        pack.addProvider(ItemBarStyleProvider::new);
    }
}
