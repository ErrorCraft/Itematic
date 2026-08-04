package net.errorcraft.itematic.client.item.bar;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ItemBarStyleLoader implements PreparableReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final FileToIdConverter FINDER = FileToIdConverter.json("item_bar_style");
    private final Map<Identifier, ItemBarStyle> styles = new HashMap<>();

    @Override
    public CompletableFuture<Void> reload(SharedState store, Executor prepareExecutor, PreparationBarrier reloadSynchronizer, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> FINDER.listMatchingResources(store.resourceManager()), prepareExecutor)
            .thenCompose(reloadSynchronizer::wait)
            .thenAcceptAsync(this::apply, applyExecutor);
    }

    public Optional<ItemBarStyle> get(Identifier id) {
        return Optional.ofNullable(this.styles.get(id));
    }

    private void apply(Map<Identifier, Resource> entries) {
        this.styles.clear();
        for (Map.Entry<Identifier, Resource> entry : entries.entrySet()) {
            Identifier path = entry.getKey();
            Identifier id = FINDER.fileToId(path);
            Resource resource = entry.getValue();
            try {
                try (BufferedReader reader = resource.openAsReader()) {
                    JsonElement json = JsonParser.parseReader(reader);
                    ItemBarStyle style = ItemBarStyle.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
                    this.styles.put(id, style);
                }
            } catch (Exception exception) {
                LOGGER.error("Failed to parse item bar style {} in resource pack {}", id, resource.sourcePackId(), exception);
            }
        }
    }
}
