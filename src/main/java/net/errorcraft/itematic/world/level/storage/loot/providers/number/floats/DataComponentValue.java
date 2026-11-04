package net.errorcraft.itematic.world.level.storage.loot.providers.number.floats;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.provider.number.floats.FloatProvider;
import net.errorcraft.itematic.mixin.world.level.storage.loot.functions.CopyComponentsFunctionAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextArg;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

import java.util.Optional;
import java.util.function.Supplier;

public record DataComponentValue<C>(FloatProvider<?, C> provider, LootContextArg<DataComponentGetter> source, Optional<Holder<ContextFloatProvider>> fallback) implements ContextFloatProvider {
    public static final MapCodec<DataComponentValue<?>> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        FloatProvider.MAP_CODEC.forGetter(DataComponentValue::provider),
        CopyComponentsFunctionAccessor.getterCodec().fieldOf("source").forGetter(DataComponentValue::source),
        ContextFloatProviders.CODEC.optionalFieldOf("fallback").forGetter(DataComponentValue::fallback)
    ).apply(instance, DataComponentValue::new));

    @Override
    public float getFloatUnsafe(LootContext context) throws ArithmeticException {
        DataComponentGetter components = this.source.get(context);
        if (components == null) {
            return this.fallbackOrThrow(context, () -> new ArithmeticException("Data component source is not present"));
        }

        try {
            C component = FloatProvider.getComponentOrThrow(components, this.provider.component());
            return this.provider.get(component, components);
        } catch (ArithmeticException e) {
            return this.fallbackOrThrow(context, () -> e);
        }
    }

    @Override
    public MapCodec<? extends ContextFloatProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public void validate(ValidationContext context) {
        this.fallback.map(Holder::value)
            .ifPresent(provider -> provider.validate(context));
    }

    private float fallbackOrThrow(LootContext context, Supplier<ArithmeticException> exceptionSupplier) throws ArithmeticException {
        return this.fallback.orElseThrow(exceptionSupplier)
            .value()
            .getFloatUnsafe(context);
    }
}
