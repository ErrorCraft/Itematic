package net.errorcraft.itematic.client.resources.item.bar.progress;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.client.resources.item.bar.progress.provider.DamageProgressProvider;
import net.errorcraft.itematic.client.resources.item.bar.progress.provider.ItemHolderOccupancyProgressProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public interface ProgressProvider {
    ExtraCodecs.LateBoundIdMapper<Identifier, ProgressProvider> ID_TO_PROVIDER = new ExtraCodecs.LateBoundIdMapper<>();
    Codec<ProgressProvider> CODEC = ID_TO_PROVIDER.codec(Identifier.CODEC);
    ProgressProvider DAMAGE = register(
        "damage",
        new DamageProgressProvider()
    );
    ProgressProvider ITEM_HOLDER_OCCUPANCY = register(
        "item_holder_occupancy",
        new ItemHolderOccupancyProgressProvider()
    );

    boolean isVisible(ItemStack stack);
    float get(ItemStack stack);

    private static ProgressProvider register(String id, ProgressProvider provider) {
        ID_TO_PROVIDER.put(Identifier.withDefaultNamespace(id), provider);
        return provider;
    }
}
