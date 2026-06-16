package net.errorcraft.itematic.predicate.item.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.NumberRangeUtil;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.random.Random;

import java.util.function.BiFunction;

public abstract class NumericalEnchantmentEffectPredicate<T> implements EnchantmentEffectPredicate {
    protected final float baseValue;
    protected final NumberRangeUtil.FloatRange range;

    public NumericalEnchantmentEffectPredicate(float baseValue, NumberRangeUtil.FloatRange range) {
        this.baseValue = baseValue;
        this.range = range;
    }

    protected static <T, A extends NumericalEnchantmentEffectPredicate<T>> Codec<A> createCodec(BiFunction<Float, NumberRangeUtil.FloatRange, A> creator) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("base_value").forGetter(predicate -> predicate.baseValue),
            NumberRangeUtil.FloatRange.CODEC.fieldOf("expected_range").forGetter(predicate -> predicate.range)
        ).apply(instance, creator));
    }

    protected static <T, A extends NumericalEnchantmentEffectPredicate<T>> PacketCodec<RegistryByteBuf, A> createPacketCodec(BiFunction<Float, NumberRangeUtil.FloatRange, A> creator) {
        return PacketCodec.tuple(
            PacketCodecs.FLOAT, predicate -> predicate.baseValue,
            NumberRangeUtil.FloatRange.PACKET_CODEC, predicate -> predicate.range,
            creator
        );
    }

    @Override
    public boolean test(ItemStack stack, Random random) {
        float value = this.getValue(this.baseValue, stack, random);
        return this.range.test(value);
    }

    public abstract float getValue(float baseValue, ItemStack stack, Random random);
    public abstract ComponentType<T> componentType();
}
