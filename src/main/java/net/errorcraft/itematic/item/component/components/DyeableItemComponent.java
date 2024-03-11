package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;

public record DyeableItemComponent(int defaultColor) implements ItemComponent<DyeableItemComponent> {
    public static final Codec<DyeableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("default_color").forGetter(DyeableItemComponent::defaultColor)
    ).apply(instance, DyeableItemComponent::new));

    @Override
    public ItemComponentType<DyeableItemComponent> type() {
        return ItemComponentTypes.DYEABLE;
    }

    @Override
    public Codec<DyeableItemComponent> codec() {
        return CODEC;
    }

    public int getColor(ItemStack stack) {
        return DyedColorComponent.getColor(stack, this.defaultColor);
    }

    public static DyeableItemComponent of() {
        return of(DyedColorComponent.field_49314);
    }

    public static DyeableItemComponent of(int defaultColor) {
        return new DyeableItemComponent(defaultColor);
    }
}
