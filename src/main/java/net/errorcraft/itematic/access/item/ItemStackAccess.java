package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ItemStackAccess {
    default ResourceKey<Item> itematic$key() {
        return null;
    }
    default void itematic$setComponents(PatchedDataComponentMap components) {}
    default void itematic$tryIncrement(int count) {}
    default int itematic$tryDecrement(int amount) {
        return 0;
    }
    default ItemStack itematic$copyOrSplit(@Nullable LivingEntity holder, int amount) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$copyWithItem(Holder<Item> item) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$copyComponentsToNewStack(Holder<Item> item, int count) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$copyComponentsToNewStackIgnoreEmpty(Holder<Item> item, int count) {
        return ItemStack.EMPTY;
    }
    default boolean itematic$isOf(ResourceKey<Item> key) {
        return false;
    }
    default void itematic$damage(int amount, ActionContext context) {}
    default <T extends ItemComponent<T>> boolean itematic$hasBehavior(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent<T>> Optional<T> itematic$getBehavior(ItemComponentType<T> type) {
        return Optional.empty();
    }
    default boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return false;
    }
    default boolean itematic$hasEventListener(ItemEvent event) {
        return false;
    }
    default boolean itematic$mayStartUsing(Level world, Player user, InteractionHand hand, ItemStack stack) {
        return false;
    }
    default double itematic$attackSpeedMultiplier() {
        return 0.0d;
    }
}
