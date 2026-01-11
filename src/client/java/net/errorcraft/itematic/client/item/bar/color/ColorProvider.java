package net.errorcraft.itematic.client.item.bar.color;

import com.mojang.serialization.Codec;

public interface ColorProvider {
    Codec<ColorProvider> CODEC = ColorProviderTypes.CODEC.dispatch(ColorProvider::type, ColorProviderType::codec);

    ColorProviderType<?> type();
    int get(float progress);
}
