package net.errorcraft.itematic.data.client;

import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceManager;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AtlasProvider extends FabricCodecDataProvider<List<AtlasSource>> {
    public AtlasProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.RESOURCE_PACK, "atlases", AtlasSourceManager.LIST_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, List<AtlasSource>> provider, RegistryWrapper.WrapperLookup lookup) {
        ItematicTexturedRenderLayers.bootstrap(provider);
    }

    @Override
    public String getName() {
        return "Atlases";
    }
}
