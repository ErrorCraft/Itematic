package net.errorcraft.itematic.core.component.provider.number.floats.providers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.provider.number.floats.FloatProvider;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;

public abstract class DamageProvider implements FloatProvider<DamageProvider, Integer> {
    public static final Type<DamageProvider, Integer> TYPE = Type.mapped(
        DamageProvider::subCodec,
        builder -> builder
            .put("damage_percentage", DamagePercentage.CODEC)
    );

    @Override
    public Type<DamageProvider, Integer> type() {
        return TYPE;
    }

    @Override
    public DataComponentType<Integer> component() {
        return DataComponents.DAMAGE;
    }

    protected abstract MapCodec<? extends DamageProvider> subCodec();

    public static class DamagePercentage extends DamageProvider {
        public static final DamagePercentage INSTANCE = new DamagePercentage();
        public static final MapCodec<DamagePercentage> CODEC = MapCodec.unit(INSTANCE);

        private DamagePercentage() {}

        @Override
        protected MapCodec<? extends DamageProvider> subCodec() {
            return CODEC;
        }

        @Override
        public float get(Integer component, DataComponentGetter components) throws ArithmeticException {
            int maxDamage = IntProvider.getComponentOrThrow(components, DataComponents.MAX_DAMAGE);
            return (float) component / maxDamage;
        }
    }
}
