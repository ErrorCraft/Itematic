package net.errorcraft.itematic.data.client;

import net.errorcraft.itematic.client.resources.item.bar.ItemBarStyle;
import net.errorcraft.itematic.client.resources.item.bar.ItemBarStyles;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ItemBarStyleProvider extends FabricCodecDataProvider<ItemBarStyle> {
    public ItemBarStyleProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "item_bar_style", ItemBarStyle.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, ItemBarStyle> provider, HolderLookup.Provider lookup) {
        ItemBarStyles.bootstrap(provider);
    }

    @Override
    public String getName() {
        return "Item Bar Styles";
    }
}
