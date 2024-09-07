package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.util.dynamic.Codecs;

public record StackableItemComponent(int maxStackSize) implements ItemComponent<StackableItemComponent> {
    public static final Codec<StackableItemComponent> CODEC = Codecs.rangedInt(1, Item.MAX_MAX_COUNT).xmap(StackableItemComponent::new, StackableItemComponent::maxStackSize);

    public static StackableItemComponent of(int maxStackSize) {
        return new StackableItemComponent(maxStackSize);
    }

    @Override
    public ItemComponentType<StackableItemComponent> type() {
        return ItemComponentTypes.STACKABLE;
    }

    @Override
    public Codec<StackableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.MAX_STACK_SIZE, this.maxStackSize);
    }
}
