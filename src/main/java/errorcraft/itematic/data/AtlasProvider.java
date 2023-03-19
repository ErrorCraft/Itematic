package errorcraft.itematic.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import errorcraft.itematic.client.render.TexturedRenderLayersUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceManager;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AtlasProvider implements DataProvider {
    private final DataOutput.PathResolver atlasPathResolver;

    public AtlasProvider(FabricDataOutput dataOutput) {
        this.atlasPathResolver = dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, "atlases");
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Identifier, JsonElement> atlasSources = new HashMap<>();
        BiConsumer<Identifier, List<AtlasSource>> consumer = (id, list) -> {
            DataResult<JsonElement> dataResult = AtlasSourceManager.LIST_CODEC.encodeStart(JsonOps.INSTANCE, list);
            JsonElement json = dataResult.result().orElseThrow(() -> new IllegalArgumentException("Invalid atlas source " + id));
            JsonElement existingJson = atlasSources.put(id, json);
            if (existingJson != null) {
                throw new IllegalArgumentException("Duplicate atlas source " + id);
            }
        };

        TexturedRenderLayersUtil.bootstrap(consumer);
        return this.write(writer, atlasSources);
    }

    private CompletableFuture<?> write(DataWriter writer, Map<Identifier, JsonElement> atlasSources) {
        return CompletableFuture.allOf(atlasSources.entrySet().stream().map(entry -> {
            Path path = this.atlasPathResolver.resolveJson(entry.getKey());
            return DataProvider.writeToPath(writer, entry.getValue(), path);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Atlases";
    }
}
