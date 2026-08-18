package net.errorcraft.itematic.world.item.use.duration.provider;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.ConstantUseDurationProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public interface UseDurationProvider {
    Codec<UseDurationProvider> ELEMENT_CODEC = ItematicBuiltInRegistries.USE_DURATION_PROVIDER_TYPE.byNameCodec()
        .dispatch(UseDurationProvider::type, UseDurationProviderType::codec);
    Codec<UseDurationProvider> CODEC = Codec.withAlternative(
        ELEMENT_CODEC,
        ExtraCodecs.POSITIVE_INT,
        ConstantUseDurationProvider::new
    );
    StreamCodec<RegistryFriendlyByteBuf, UseDurationProvider> STREAM_CODEC = ByteBufCodecs.registry(ItematicRegistries.USE_DURATION_PROVIDER_TYPE)
        .dispatch(UseDurationProvider::type, UseDurationProviderType::streamCodec);

    UseDurationProviderType<?> type();
    OptionalInt get(ItemStack stack, LivingEntity user);
}
