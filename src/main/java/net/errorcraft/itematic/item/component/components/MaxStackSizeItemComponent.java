package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.util.dynamic.Codecs;

public record MaxStackSizeItemComponent(int maxStackSize) implements ItemComponent<MaxStackSizeItemComponent> {
    public static final Codec<MaxStackSizeItemComponent> CODEC = Codecs.rangedInt(1, Item.MAX_MAX_COUNT).xmap(MaxStackSizeItemComponent::new, MaxStackSizeItemComponent::maxStackSize);

    @Override
    public ItemComponentType<MaxStackSizeItemComponent> type() {
        return ItemComponentTypes.MAX_STACK_SIZE;
    }

    @Override
    public Codec<MaxStackSizeItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.MAX_STACK_SIZE, this.maxStackSize);
    }

    public static MaxStackSizeItemComponent of(int maxCount) {
        return new MaxStackSizeItemComponent(maxCount);
    }
}
