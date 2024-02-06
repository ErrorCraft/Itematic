package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public record DyeableItemComponent(int defaultColor) implements ItemComponent<DyeableItemComponent>, DyeableItem {
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
        NbtCompound display = stack.getSubNbt(DyeableItem.DISPLAY_KEY);
        if (display != null && display.contains(DyeableItem.COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return display.getInt(DyeableItem.COLOR_KEY);
        }
        return this.defaultColor;
    }
}
