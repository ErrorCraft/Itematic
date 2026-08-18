package net.errorcraft.itematic.access.world.item;

import net.errorcraft.itematic.world.action.ActionEventMap;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemDisplay;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorSet;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Consumer;

public interface ItemAccess {
    default ItemDisplay itematic$display() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setDisplay(ItemDisplay display) {}
    default ItemAttributeModifiers itematic$attributeModifiers() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setAttributeModifiers(ItemAttributeModifiers attributeModifiers) {}
    default ItemBehaviorSet itematic$behavior() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setBehavior(ItemBehaviorSet components) {}
    default <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        return false;
    }
    default <T extends ItemBehavior<T>> Optional<T> itematic$getBehavior(ItemBehaviorType<T> type) {
        return Optional.empty();
    }
    default ActionEventMap<ItemEvent> itematic$events() {
        return ActionEventMap.empty();
    }
    default void itematic$setEvents(ActionEventMap<ItemEvent> events) {}
    default boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return false;
    }
    default boolean itematic$hasEventListener(ItemEvent event) {
        return false;
    }
    default void itematic$addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag tooltipFlag) {}
    default boolean itematic$mayStartUsing(Level world, Player user, InteractionHand hand, ItemStack stack) {
        return true;
    }
}
