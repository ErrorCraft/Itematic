package net.errorcraft.itematic.client.resources.item.bar.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.client.resources.item.bar.color.provider.ConstantColorProvider;
import net.errorcraft.itematic.client.resources.item.bar.color.provider.FirstToPassConditionColorProvider;
import net.errorcraft.itematic.client.resources.item.bar.color.provider.HueShiftColorProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public record ColorProviderType<T extends ColorProvider>(MapCodec<T> codec) {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, ColorProviderType<?>> ID_TO_TYPE = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<ColorProviderType<?>> CODEC = ID_TO_TYPE.codec(Identifier.CODEC);
    public static final ColorProviderType<ConstantColorProvider> CONSTANT = register(
        "constant",
        ConstantColorProvider.CODEC
    );
    public static final ColorProviderType<HueShiftColorProvider> HUE_SHIFT = register(
        "hue_shift",
        HueShiftColorProvider.CODEC
    );
    public static final ColorProviderType<FirstToPassConditionColorProvider> FIRST_TO_PASS_CONDITION = register(
        "first_to_pass_condition",
        FirstToPassConditionColorProvider.CODEC
    );

    private static <T extends ColorProvider> ColorProviderType<T> register(String id, MapCodec<T> codec) {
        ColorProviderType<T> type = new ColorProviderType<>(codec);
        ID_TO_TYPE.put(Identifier.withDefaultNamespace(id), type);
        return type;
    }
}
