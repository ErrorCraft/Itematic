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

public record DataComponentValue<C>(FloatProvider<?, C> provider, LootContextArg<DataComponentGetter> source, Holder<ContextFloatProvider> fallback) implements ContextFloatProvider {
    public static final MapCodec<DataComponentValue<?>> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        FloatProvider.MAP_CODEC.forGetter(DataComponentValue::provider),
        CopyComponentsFunctionAccessor.getterCodec().fieldOf("source").forGetter(DataComponentValue::source),
        ContextFloatProviders.CODEC.optionalFieldOf("fallback", ContextFloatProviders.exactly(0)).forGetter(DataComponentValue::fallback)
    ).apply(instance, DataComponentValue::new));

    @Override
    public float getFloatUnsafe(LootContext context) throws ArithmeticException {
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
    public MapCodec<? extends ContextFloatProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public void validate(ValidationContext context) {
        this.fallback.value().validate(context);
    }

    private float fallback(LootContext context) {
        return this.fallback.value().getFloatUnsafe(context);
    }
}
