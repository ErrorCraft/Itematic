package net.errorcraft.itematic.item.use.provider;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.OptionalInt;

public interface IntegerProvider {
    Codec<IntegerProvider> ELEMENT_CODEC = ItematicRegistries.INTEGER_PROVIDER_TYPE.byNameCodec().dispatch("type", IntegerProvider::type, IntegerProviderType::codec);;
    Codec<IntegerProvider> CODEC = Codec.withAlternative(
        ELEMENT_CODEC,
        ExtraCodecs.POSITIVE_INT,
        ConstantIntegerProvider::new
    );
    StreamCodec<RegistryFriendlyByteBuf, IntegerProvider> PACKET_CODEC = ByteBufCodecs.registry(ItematicRegistryKeys.INTEGER_PROVIDER_TYPE)
        .dispatch(IntegerProvider::type, IntegerProviderType::packetCodec);

    IntegerProviderType<?> type();
    OptionalInt get(ItemStack stack, LivingEntity user);
}
