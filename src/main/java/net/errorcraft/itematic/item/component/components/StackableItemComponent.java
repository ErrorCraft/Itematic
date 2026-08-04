package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record StackableItemComponent(int maxStackSize) implements ItemComponent<StackableItemComponent> {
    public static final Codec<StackableItemComponent> CODEC = ExtraCodecs.intRange(1, Item.ABSOLUTE_MAX_STACK_SIZE).xmap(StackableItemComponent::new, StackableItemComponent::maxStackSize);

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
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.MAX_STACK_SIZE, this.maxStackSize);
    }
}
