package net.errorcraft.itematic.item.use.provider;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

import java.util.OptionalInt;

public interface IntegerProvider {
    Codec<IntegerProvider> ELEMENT_CODEC = ItematicRegistries.INTEGER_PROVIDER_TYPE.getCodec().dispatch("type", IntegerProvider::type, IntegerProviderType::codec);;
    Codec<IntegerProvider> CODEC = Codec.withAlternative(
        ELEMENT_CODEC,
        Codecs.POSITIVE_INT,
        ConstantIntegerProvider::new
    );
    PacketCodec<RegistryByteBuf, IntegerProvider> PACKET_CODEC = PacketCodecs.registryValue(ItematicRegistryKeys.INTEGER_PROVIDER_TYPE)
        .dispatch(IntegerProvider::type, IntegerProviderType::packetCodec);

    IntegerProviderType<?> type();
    OptionalInt get(ItemStack stack);
}
