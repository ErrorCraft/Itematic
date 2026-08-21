package net.errorcraft.itematic.world.item.use.duration.provider;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.ConditionUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.ConstantUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.IndefiniteUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.PlayableUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.ShooterUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.TridentUseDurationProvider;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record UseDurationProviderType<T extends UseDurationProvider>(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    public static final UseDurationProviderType<ConstantUseDurationProvider> CONSTANT = register(
        "constant",
        new UseDurationProviderType<>(
            ConstantUseDurationProvider.CODEC,
            ConstantUseDurationProvider.STREAM_CODEC
        )
    );
    public static final UseDurationProviderType<PlayableUseDurationProvider> PLAYABLE = register(
        "playable",
        new UseDurationProviderType<>(
            PlayableUseDurationProvider.CODEC,
            PlayableUseDurationProvider.STREAM_CODEC
        )
    );
    public static final UseDurationProviderType<ShooterUseDurationProvider> SHOOTER = register(
        "shooter",
        new UseDurationProviderType<>(
            ShooterUseDurationProvider.CODEC,
            ShooterUseDurationProvider.STREAM_CODEC
        )
    );
    public static final UseDurationProviderType<TridentUseDurationProvider> TRIDENT = register(
        "trident",
        new UseDurationProviderType<>(
            TridentUseDurationProvider.CODEC,
            TridentUseDurationProvider.STREAM_CODEC
        )
    );
    public static final UseDurationProviderType<ConditionUseDurationProvider> CONDITION = register(
        "condition",
        new UseDurationProviderType<>(
            ConditionUseDurationProvider.CODEC,
            ConditionUseDurationProvider.STREAM_CODEC
        )
    );
    public static final UseDurationProviderType<IndefiniteUseDurationProvider> INDEFINITE = register(
        "indefinite",
        new UseDurationProviderType<>(
            IndefiniteUseDurationProvider.CODEC,
            IndefiniteUseDurationProvider.STREAM_CODEC
        )
    );

    public static void init() {}

    private static <T extends UseDurationProvider> UseDurationProviderType<T> register(String id, UseDurationProviderType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.USE_DURATION_PROVIDER_TYPE, id, type);
    }
}
