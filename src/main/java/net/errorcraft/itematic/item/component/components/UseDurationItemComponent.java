package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.util.dynamic.Codecs;

public record UseDurationItemComponent(int ticks) implements ItemComponent<UseDurationItemComponent> {
    public static final Codec<UseDurationItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("ticks").forGetter(UseDurationItemComponent::ticks)
    ).apply(instance, UseDurationItemComponent::new));

    @Override
    public ItemComponentType<UseDurationItemComponent> type() {
        return ItemComponentTypes.USE_DURATION;
    }

    @Override
    public Codec<UseDurationItemComponent> codec() {
        return CODEC;
    }

    public static UseDurationItemComponent of(int ticks) {
        return new UseDurationItemComponent(ticks);
    }
}
