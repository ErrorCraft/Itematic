package net.errorcraft.itematic.client.resources.item.bar.color.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.client.resources.item.bar.color.ColorProvider;
import net.errorcraft.itematic.client.resources.item.bar.color.ColorProviderType;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.minecraft.util.Mth;

public record HueShiftColorProvider(int start, int end) implements ColorProvider {
    public static final MapCodec<HueShiftColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicCodecs.HUE.fieldOf("start").forGetter(HueShiftColorProvider::start),
        ItematicCodecs.HUE.fieldOf("end").forGetter(HueShiftColorProvider::end)
    ).apply(instance, HueShiftColorProvider::new));

    @Override
    public ColorProviderType<?> type() {
        return ColorProviderType.HUE_SHIFT;
    }

    @Override
    public int get(float progress) {
        int hue = Mth.lerpInt(progress, this.start, this.end);
        return Mth.hsvToRgb(hue / 360.0f, 1.0f, 1.0f);
    }
}
