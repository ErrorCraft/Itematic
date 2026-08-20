package net.errorcraft.itematic.client.resources.item.bar.color;

import com.mojang.serialization.Codec;

public interface ColorProvider {
    Codec<ColorProvider> CODEC = Codec.lazyInitialized(() -> ColorProviderType.CODEC.dispatch(
        ColorProvider::type,
        ColorProviderType::codec
    ));

    ColorProviderType<?> type();
    int get(float progress);
}
