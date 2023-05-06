package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;

public record DyeableItemComponent(int defaultColor) implements ItemComponent {
    public static final Codec<DyeableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("default_color").forGetter(DyeableItemComponent::defaultColor)
    ).apply(instance, DyeableItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.DYEABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }
}
