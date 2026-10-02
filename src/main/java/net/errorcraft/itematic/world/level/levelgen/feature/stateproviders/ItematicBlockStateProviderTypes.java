package net.errorcraft.itematic.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ItematicBlockStateProviderTypes {
    private ItematicBlockStateProviderTypes() {}

    public static void bootstrap(Registry<MapCodec<? extends BlockStateProvider>> registry) {
        Registry.register(registry, "apply_properties", ApplyPropertiesProvider.CODEC);
        Registry.register(registry, "map_block", MapBlockProvider.CODEC);
    }
}
