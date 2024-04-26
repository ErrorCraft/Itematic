package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;

public record TintedItemComponent(ItemColor<?> tint) implements ItemComponent<TintedItemComponent> {
    public static final Codec<TintedItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemColor.CODEC.fieldOf("tint").forGetter(TintedItemComponent::tint)
    ).apply(instance, TintedItemComponent::new));

    public static TintedItemComponent of(ItemColor<?> tint) {
        return new TintedItemComponent(tint);
    }

    @Override
    public ItemComponentType<TintedItemComponent> type() {
        return ItemComponentTypes.TINTED;
    }

    @Override
    public Codec<TintedItemComponent> codec() {
        return CODEC;
    }
}
