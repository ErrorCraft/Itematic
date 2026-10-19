package net.errorcraft.itematic.core.component.provider.number.ints.providers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;

public abstract class FoodProvider implements IntProvider<FoodProvider, FoodProperties> {
    public static final Type<FoodProvider, FoodProperties> TYPE = Type.mapped(
        FoodProvider::subCodec,
        builder -> builder
            .put("nutrition", Nutrition.CODEC)
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

    public static class Nutrition extends FoodProvider {
        public static final Nutrition INSTANCE = new Nutrition();
        public static final MapCodec<Nutrition> CODEC = MapCodec.unit(INSTANCE);

        private Nutrition() {}

        @Override
        public MapCodec<? extends FoodProvider> subCodec() {
            return CODEC;
        }

        @Override
        public int get(FoodProperties component, DataComponentGetter components) throws ArithmeticException {
            return component.nutrition();
        }
    }
}
