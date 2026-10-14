package net.errorcraft.itematic.core.component.provider.number.floats.providers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.provider.number.floats.FloatProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;

public abstract class FoodProvider implements FloatProvider<FoodProvider, FoodProperties> {
    public static final Type<FoodProvider, FoodProperties> TYPE = Type.mapped(
        FoodProvider::subCodec,
        builder -> builder
            .put("saturation", Saturation.CODEC)
    );

    @Override
    public Type<FoodProvider, FoodProperties> type() {
        return TYPE;
    }

    @Override
    public DataComponentType<FoodProperties> component() {
        return DataComponents.FOOD;
    }

    protected abstract MapCodec<? extends FoodProvider> subCodec();

    public static class Saturation extends FoodProvider {
        public static final Saturation INSTANCE = new Saturation();
        public static final MapCodec<Saturation> CODEC = MapCodec.unit(INSTANCE);

        private Saturation() {}

        @Override
        public MapCodec<? extends FoodProvider> subCodec() {
            return CODEC;
        }

        @Override
        public float get(FoodProperties component, DataComponentGetter components) throws ArithmeticException {
            return component.saturation();
        }
    }
}
