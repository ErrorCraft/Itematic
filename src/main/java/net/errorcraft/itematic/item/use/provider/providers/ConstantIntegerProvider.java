package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

import java.util.OptionalInt;

public record ConstantIntegerProvider(int value) implements IntegerProvider {
    public static final MapCodec<ConstantIntegerProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("amount").forGetter(ConstantIntegerProvider::value)
    ).apply(instance, ConstantIntegerProvider::new));
    public static final PacketCodec<ByteBuf, ConstantIntegerProvider> PACKET_CODEC = PacketCodecs.VAR_INT.xmap(ConstantIntegerProvider::new, ConstantIntegerProvider::value);

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.CONSTANT;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(this.value);
    }
}
