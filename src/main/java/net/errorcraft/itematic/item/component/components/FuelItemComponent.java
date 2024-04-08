package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.util.dynamic.Codecs;

public record FuelItemComponent(int ticks) implements ItemComponent<FuelItemComponent> {
    public static final Codec<FuelItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("ticks").forGetter(FuelItemComponent::ticks)
    ).apply(instance, FuelItemComponent::new));

    @Override
    public ItemComponentType<FuelItemComponent> type() {
        return ItemComponentTypes.FUEL;
    }

    @Override
    public Codec<FuelItemComponent> codec() {
        return CODEC;
    }

    public static FuelItemComponent of(int ticks) {
        return new FuelItemComponent(ticks);
    }
}
