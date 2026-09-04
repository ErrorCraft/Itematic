package net.errorcraft.itematic.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

public class ItematicBlockStateProviderTypes {
    public static final BlockStateProviderType<ApplyPropertiesProvider> APPLY_PROPERTIES = register(
        "apply_properties",
        ApplyPropertiesProvider.CODEC
    );

    private ItematicBlockStateProviderTypes() {}

    public static void init() {}

    private static <P extends BlockStateProvider> BlockStateProviderType<P> register(final String name, final MapCodec<P> codec) {
        return Registry.register(
            BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE,
            name,
            new BlockStateProviderType<>(codec)
        );
    }
}
