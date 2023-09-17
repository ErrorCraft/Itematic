package net.errorcraft.itematic.data.client;

import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceManager;
import net.minecraft.data.DataOutput;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiConsumer;

public class AtlasProvider extends FabricCodecDataProvider<List<AtlasSource>> {
    public AtlasProvider(FabricDataOutput dataOutput) {
        super(dataOutput, DataOutput.OutputType.RESOURCE_PACK, "atlases", AtlasSourceManager.LIST_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, List<AtlasSource>> provider) {
        ItematicTexturedRenderLayers.bootstrap(provider);
    }

    @Override
    public String getName() {
        return "Atlases";
    }
}
