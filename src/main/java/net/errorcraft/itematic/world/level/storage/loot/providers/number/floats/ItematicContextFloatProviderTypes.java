package net.errorcraft.itematic.world.level.storage.loot.providers.number.floats;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public class ItematicContextFloatProviderTypes {
    private ItematicContextFloatProviderTypes() {}

    public static void bootstrap(Registry<MapCodec<? extends ContextFloatProvider>> registry) {
        Registry.register(registry, "data_component", DataComponentValue.MAP_CODEC);
    }
}
