package net.errorcraft.itematic.client.item.bar.color.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.client.item.bar.color.ColorProvider;
import net.errorcraft.itematic.client.item.bar.color.ColorProviderType;
import net.errorcraft.itematic.client.item.bar.color.ColorProviderTypes;

public record ConstantColorProvider(int color) implements ColorProvider {
    public static final MapCodec<ConstantColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color").forGetter(ConstantColorProvider::color)
    ).apply(instance, ConstantColorProvider::new));

    @Override
    public ColorProviderType<?> type() {
        return ColorProviderTypes.CONSTANT;
    }

    @Override
    public int get(float progress) {
        return this.color;
    }
}
