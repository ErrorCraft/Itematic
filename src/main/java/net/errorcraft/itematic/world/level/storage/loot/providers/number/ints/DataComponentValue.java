package net.errorcraft.itematic.world.level.storage.loot.providers.number.ints;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.errorcraft.itematic.mixin.world.level.storage.loot.functions.CopyComponentsFunctionAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextArg;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public record DataComponentValue<C>(IntProvider<?, C> provider, LootContextArg<DataComponentGetter> source, Holder<ContextIntProvider> fallback) implements ContextIntProvider {
    public static final MapCodec<DataComponentValue<?>> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        IntProvider.MAP_CODEC.forGetter(DataComponentValue::provider),
        CopyComponentsFunctionAccessor.getterCodec().fieldOf("source").forGetter(DataComponentValue::source),
        ContextIntProviders.CODEC.optionalFieldOf("fallback", ContextIntProviders.exactly(0)).forGetter(DataComponentValue::fallback)
    ).apply(instance, DataComponentValue::new));

    public static <C> DataComponentValue<C> of(IntProvider<?, C> provider, ContextKey<? extends DataComponentGetter> source) {
        return new DataComponentValue<>(
            provider,
            new CopyComponentsFunction.DirectSource<>(source),
            ContextIntProviders.exactly(0)
        );
    }

    @Override
    public int getIntUnsafe(LootContext context) throws ArithmeticException {
        DataComponentGetter components = this.source.get(context);
        if (components == null) {
            return this.fallback(context);
        }

        C component = components.get(this.provider.component());
        if (component == null) {
            return this.fallback(context);
        }

        return this.provider.get(component, components);
    }

    @Override
    public MapCodec<? extends ContextIntProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public void validate(ValidationContext context) {}

    private int fallback(LootContext context) {
        return this.fallback.value().getIntUnsafe(context);
    }
}
