package net.errorcraft.itematic.core.component.provider.number.ints.providers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;

public abstract class DamageProvider implements IntProvider<DamageProvider, Integer> {
    public static final Type<DamageProvider, Integer> TYPE = Type.mapped(
        DamageProvider::subCodec,
        builder -> builder
            .put("damage", Damage.CODEC)
            .put("durability", Durability.CODEC)
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

    public static class Damage extends DamageProvider {
        public static final Damage INSTANCE = new Damage();
        public static final MapCodec<Damage> CODEC = MapCodec.unit(INSTANCE);

        private Damage() {}

        @Override
        protected MapCodec<? extends DamageProvider> subCodec() {
            return CODEC;
        }

        @Override
        public int get(Integer component, DataComponentGetter components) throws ArithmeticException {
            return component;
        }
    }

    public static class Durability extends DamageProvider {
        public static final Durability INSTANCE = new Durability();
        public static final MapCodec<Durability> CODEC = MapCodec.unit(INSTANCE);

        private Durability() {}

        @Override
        protected MapCodec<? extends DamageProvider> subCodec() {
            return CODEC;
        }

        @Override
        public int get(Integer component, DataComponentGetter components) throws ArithmeticException {
            int maxDamage = IntProvider.getComponentOrThrow(components, DataComponents.MAX_DAMAGE);
            return maxDamage - component;
        }
    }
}
