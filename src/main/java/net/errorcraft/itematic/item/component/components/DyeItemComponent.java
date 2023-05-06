package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.util.DyeColor;

public record DyeItemComponent(DyeColor color) implements ItemComponent {
    public static final Codec<DyeItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.fieldOf("color").forGetter(DyeItemComponent::color)
    ).apply(instance, DyeItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.DYE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }
}
