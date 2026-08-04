package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.OptionalInt;

public record ConstantIntegerProvider(int value) implements IntegerProvider {
    public static final MapCodec<ConstantIntegerProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(ConstantIntegerProvider::value)
    ).apply(instance, ConstantIntegerProvider::new));
    public static final StreamCodec<ByteBuf, ConstantIntegerProvider> PACKET_CODEC = ByteBufCodecs.VAR_INT.map(ConstantIntegerProvider::new, ConstantIntegerProvider::value);

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.CONSTANT;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(this.value);
    }
}
