package net.errorcraft.itematic.client.item.bar.progress;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.client.item.bar.progress.provider.DamageProgressProvider;
import net.errorcraft.itematic.client.item.bar.progress.provider.ItemHolderOccupancyProgressProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public interface ProgressProvider {
    Map<Identifier, ProgressProvider> ID_TO_PROVIDER = new HashMap<>();
    Codec<ProgressProvider> CODEC = Identifier.CODEC.comapFlatMap(id -> {
        ProgressProvider type = ID_TO_PROVIDER.get(id);
        return type != null ? DataResult.success(type) : DataResult.error(() -> "Unknown progress provider with id " + id);
    }, ProgressProvider::id);

    ProgressProvider DAMAGE = register(ProgressProviderKeys.DAMAGE, new DamageProgressProvider());
    ProgressProvider ITEM_HOLDER_OCCUPANCY = register(ProgressProviderKeys.ITEM_HOLDER_OCCUPANCY, new ItemHolderOccupancyProgressProvider());

    Identifier id();
    boolean isVisible(ItemStack stack);
    float get(ItemStack stack);

    private static ProgressProvider register(Identifier id, ProgressProvider provider) {
        ProgressProvider existing = ID_TO_PROVIDER.putIfAbsent(id, provider);
        if (existing != null) {
            throw new IllegalStateException("Duplicate progress provider registration with id " + id);
        }

        return provider;
    }
}
