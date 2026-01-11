package net.errorcraft.itematic.data.client;

import net.errorcraft.itematic.client.item.bar.ItemBarStyle;
import net.errorcraft.itematic.client.item.bar.ItemBarStyles;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ItemBarStyleProvider extends FabricCodecDataProvider<ItemBarStyle> {
    public ItemBarStyleProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.RESOURCE_PACK, "item_bar_style", ItemBarStyle.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, ItemBarStyle> provider, RegistryWrapper.WrapperLookup lookup) {
        ItemBarStyles.bootstrap(provider);
    }

    @Override
    public String getName() {
        return "Item Bar Styles";
    }
}
