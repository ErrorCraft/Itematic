package net.errorcraft.itematic.core.component.provider.number.ints.providers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;

public class MaxDamageProvider implements IntProvider<MaxDamageProvider, Integer> {
    public static final MaxDamageProvider INSTANCE = new MaxDamageProvider();
    public static final MapCodec<MaxDamageProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final Type<MaxDamageProvider, Integer> TYPE = Type.simple(CODEC);

    private MaxDamageProvider() {}

    @Override
    public Type<MaxDamageProvider, Integer> type() {
        return TYPE;
    }

    @Override
    public DataComponentType<Integer> component() {
        return DataComponents.MAX_DAMAGE;
    }

    @Override
    public int get(Integer component, DataComponentGetter components) throws ArithmeticException {
        return component;
    }
}
