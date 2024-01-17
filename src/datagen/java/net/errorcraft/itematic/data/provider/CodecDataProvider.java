package net.errorcraft.itematic.data.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class CodecDataProvider<T> implements DataProvider {
    private final DataOutput.PathResolver pathResolver;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;
    private final Codec<T> codec;

    protected CodecDataProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, DataOutput.OutputType outputType, String directoryName, Codec<T> codec) {
        this.pathResolver = dataOutput.getResolver(outputType, directoryName);
        this.registriesFuture = registriesFuture;
        this.codec = codec;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose(lookup -> {
            Map<Identifier, JsonElement> entries = new HashMap<>();
            RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, lookup);

            BiConsumer<Identifier, T> provider = (id, value) -> {
                JsonElement json = this.convert(id, value, ops);
                JsonElement existingJson = entries.put(id, json);

                if (existingJson != null) {
                    throw new IllegalArgumentException("Duplicate entry " + id);
                }
            };

            this.configure(provider, lookup);
            return this.write(writer, entries);
        });
    }

    protected abstract void configure(BiConsumer<Identifier, T> provider, RegistryWrapper.WrapperLookup lookup);

    private JsonElement convert(Identifier id, T value, DynamicOps<JsonElement> ops) {
        DataResult<JsonElement> dataResult = this.codec.encodeStart(ops, value);
        return dataResult.get()
            .mapRight(partial -> "Invalid entry %s: %s".formatted(id, partial.message()))
            .orThrow();
    }

    private CompletableFuture<?> write(DataWriter writer, Map<Identifier, JsonElement> entries) {
        return CompletableFuture.allOf(entries.entrySet().stream().map(entry -> {
            Path path = this.pathResolver.resolveJson(entry.getKey());
            return DataProvider.writeToPath(writer, entry.getValue(), path);
        }).toArray(CompletableFuture[]::new));
    }
}
