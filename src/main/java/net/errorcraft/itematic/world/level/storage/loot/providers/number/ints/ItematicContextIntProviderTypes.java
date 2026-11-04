package net.errorcraft.itematic.world.level.storage.loot.providers.number.ints;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public class ItematicContextIntProviderTypes {
    private ItematicContextIntProviderTypes() {}

    public static void bootstrap(Registry<MapCodec<? extends ContextIntProvider>> registry) {
        Registry.register(registry, "data_component", DataComponentValue.MAP_CODEC);
    }
}
