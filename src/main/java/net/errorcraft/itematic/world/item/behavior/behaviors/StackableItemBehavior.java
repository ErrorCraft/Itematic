package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record StackableItemBehavior(int maxStackSize) implements ItemBehavior<StackableItemBehavior> {
    public static final Codec<StackableItemBehavior> CODEC = ExtraCodecs.intRange(1, Item.ABSOLUTE_MAX_STACK_SIZE).xmap(StackableItemBehavior::new, StackableItemBehavior::maxStackSize);

    public static StackableItemBehavior of(int maxStackSize) {
        return new StackableItemBehavior(maxStackSize);
    }

    @Override
    public ItemBehaviorType<StackableItemBehavior> type() {
        return ItemBehaviorType.STACKABLE;
    }

    @Override
    public Codec<StackableItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.MAX_STACK_SIZE, this.maxStackSize);
    }
}
