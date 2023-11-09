package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;

public record CompostableItemComponent(float levelIncreaseChance) implements ItemComponent {
    public static final Codec<CompostableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.floatRange(0.0f, 1.0f).fieldOf("level_increase_chance").forGetter(CompostableItemComponent::levelIncreaseChance)
    ).apply(instance, CompostableItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.COMPOSTABLE;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }
}
