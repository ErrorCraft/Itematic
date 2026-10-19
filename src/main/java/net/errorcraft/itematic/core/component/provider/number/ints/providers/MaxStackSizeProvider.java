package net.errorcraft.itematic.core.component.provider.number.ints.providers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;

public class MaxStackSizeProvider implements IntProvider<MaxStackSizeProvider, Integer> {
    public static final MaxStackSizeProvider INSTANCE = new MaxStackSizeProvider();
    public static final MapCodec<MaxStackSizeProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final Type<MaxStackSizeProvider, Integer> TYPE = Type.simple(CODEC);

    private MaxStackSizeProvider() {}

    @Override
    public Type<MaxStackSizeProvider, Integer> type() {
        return TYPE;
    }

    @Override
    public DataComponentType<Integer> component() {
        return DataComponents.MAX_STACK_SIZE;
    }

    @Override
    public int get(Integer component, DataComponentGetter components) throws ArithmeticException {
        return component;
    }
}
