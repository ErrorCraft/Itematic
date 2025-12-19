package net.errorcraft.itematic.client.item.bar.color.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.client.item.bar.color.ColorProvider;
import net.errorcraft.itematic.client.item.bar.color.ColorProviderType;
import net.errorcraft.itematic.client.item.bar.color.ColorProviderTypes;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.util.math.MathHelper;

public record HueShiftColorProvider(int start, int end) implements ColorProvider {
    public static final MapCodec<HueShiftColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicCodecs.HUE.fieldOf("start").forGetter(HueShiftColorProvider::start),
        ItematicCodecs.HUE.fieldOf("end").forGetter(HueShiftColorProvider::end)
    ).apply(instance, HueShiftColorProvider::new));

    @Override
    public ColorProviderType<?> type() {
        return ColorProviderTypes.HUE_SHIFT;
    }

    @Override
    public int get(float progress) {
        int hue = MathHelper.lerp(progress, this.start, this.end);
        return MathHelper.hsvToRgb(hue / 360.0f, 1.0f, 1.0f);
    }
}
