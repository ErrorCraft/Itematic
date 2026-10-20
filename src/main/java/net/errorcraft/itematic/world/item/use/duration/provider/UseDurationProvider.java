package net.errorcraft.itematic.world.item.use.duration.provider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Constant;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public interface UseDurationProvider {
    Codec<UseDurationProvider> FULL_CODEC = ItematicBuiltInRegistries.USE_DURATION_PROVIDER_TYPE.byNameCodec()
        .dispatch(UseDurationProvider::type, Type::codec);
    Codec<UseDurationProvider> CODEC = ItematicCodecs.withAlternative(
        FULL_CODEC,
        ExtraCodecs.POSITIVE_INT,
        Constant::new,
        UseDurationProvider::wrap
    );
    StreamCodec<RegistryFriendlyByteBuf, UseDurationProvider> STREAM_CODEC = ByteBufCodecs.registry(ItematicRegistries.USE_DURATION_PROVIDER_TYPE)
        .dispatch(UseDurationProvider::type, Type::streamCodec);

    Type<?> type();
    OptionalInt get(ItemStack stack, LivingEntity user);

    private static Either<UseDurationProvider, Integer> wrap(UseDurationProvider useDurationProvider) {
        if (useDurationProvider instanceof Constant(int ticks)) {
            return Either.right(ticks);
        }

        return Either.left(useDurationProvider);
    }

    record Type<T extends UseDurationProvider>(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {}
}
