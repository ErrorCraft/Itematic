package net.errorcraft.itematic.client.item.bar.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.client.item.bar.color.provider.ConstantColorProvider;
import net.errorcraft.itematic.client.item.bar.color.provider.FirstToPassConditionColorProvider;
import net.errorcraft.itematic.client.item.bar.color.provider.HueShiftColorProvider;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ColorProviderTypes {
    private static final Map<Identifier, ColorProviderType<?>> ID_TO_TYPE = new HashMap<>();
    public static final Codec<ColorProviderType<?>> CODEC = Identifier.CODEC.comapFlatMap(id -> {
        ColorProviderType<?> type = ID_TO_TYPE.get(id);
        return type != null ? DataResult.success(type) : DataResult.error(() -> "Unknown color provider with id " + id);
    }, ColorProviderType::id);

    public static final ColorProviderType<ConstantColorProvider> CONSTANT = register(ColorProviderTypeKeys.CONSTANT, ConstantColorProvider.CODEC);
    public static final ColorProviderType<HueShiftColorProvider> HUE_SHIFT = register(ColorProviderTypeKeys.HUE_SHIFT, HueShiftColorProvider.CODEC);
    public static final ColorProviderType<FirstToPassConditionColorProvider> FIRST_TO_PASS_CONDITION = register(ColorProviderTypeKeys.FIRST_TO_PASS_CONDITION, FirstToPassConditionColorProvider.CODEC);

    private ColorProviderTypes() {}

    private static <T extends ColorProvider> ColorProviderType<T> register(Identifier id, MapCodec<T> codec) {
        ColorProviderType<T> type = new ColorProviderType<>(id, codec);
        ColorProviderType<?> existing = ID_TO_TYPE.putIfAbsent(id, type);
        if (existing != null) {
            throw new IllegalStateException("Duplicate color provider type registration with id " + id);
        }

        return type;
    }
}
