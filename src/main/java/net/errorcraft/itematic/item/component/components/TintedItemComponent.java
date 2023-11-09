package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;

public record TintedItemComponent(ItemColor tint) implements ItemComponent {
    public static final Codec<TintedItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemColor.CODEC.fieldOf("tint").forGetter(TintedItemComponent::tint)
    ).apply(instance, TintedItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.TINTED;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }
}
