package net.errorcraft.itematic.core.component.provider.number.ints.providers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.OminousBottleAmplifier;

public class OminousBottleAmplifierProvider implements IntProvider<OminousBottleAmplifierProvider, OminousBottleAmplifier> {
    public static final OminousBottleAmplifierProvider INSTANCE = new OminousBottleAmplifierProvider();
    public static final MapCodec<OminousBottleAmplifierProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final Type<OminousBottleAmplifierProvider, OminousBottleAmplifier> TYPE = Type.simple(OminousBottleAmplifierProvider.CODEC);

    private OminousBottleAmplifierProvider() {}

    @Override
    public Type<OminousBottleAmplifierProvider, OminousBottleAmplifier> type() {
        return TYPE;
    }

    @Override
    public DataComponentType<OminousBottleAmplifier> component() {
        return DataComponents.OMINOUS_BOTTLE_AMPLIFIER;
    }

    @Override
    public int get(OminousBottleAmplifier component, DataComponentGetter components) {
        return component.value();
    }
}
