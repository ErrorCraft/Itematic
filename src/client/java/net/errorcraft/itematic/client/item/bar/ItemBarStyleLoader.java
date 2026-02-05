package net.errorcraft.itematic.client.item.bar;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ItemBarStyleLoader implements ResourceReloader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceFinder FINDER = ResourceFinder.json("item_bar_style");
    private final Map<Identifier, ItemBarStyle> styles = new HashMap<>();

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> FINDER.findResources(manager), prepareExecutor)
            .thenCompose(synchronizer::whenPrepared)
            .thenAcceptAsync(this::apply, applyExecutor);
    }

    public Optional<ItemBarStyle> get(Identifier id) {
        return Optional.ofNullable(this.styles.get(id));
    }

    private void apply(Map<Identifier, Resource> entries) {
        this.styles.clear();
        for (Map.Entry<Identifier, Resource> entry : entries.entrySet()) {
            Identifier path = entry.getKey();
            Identifier id = FINDER.toResourceId(path);
            Resource resource = entry.getValue();
            try {
                try (BufferedReader reader = resource.getReader()) {
                    JsonElement json = JsonParser.parseReader(reader);
                    ItemBarStyle style = ItemBarStyle.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
                    this.styles.put(id, style);
                }
            } catch (Exception exception) {
                LOGGER.error("Failed to parse item bar style {} in resource pack {}", id, resource.getPackId(), exception);
            }
        }
    }
}
