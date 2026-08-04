package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.ItemDisplay;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.world.action.context.ActionContext;
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
        return null;
    }
    default void itematic$setDisplay(ItemDisplay display) {}
    default ItemAttributeModifiers itematic$attributeModifiers() {
        return null;
    }
    default void itematic$setAttributeModifiers(ItemAttributeModifiers attributeModifiers) {}
    default ItemComponentSet itematic$behavior() {
        return null;
    }
    default void itematic$setBehavior(ItemComponentSet components) {}
    default <T extends ItemComponent<T>> boolean itematic$hasBehavior(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent<T>> Optional<T> itematic$getBehavior(ItemComponentType<T> type) {
        return Optional.empty();
    }
    default ItemEventMap itematic$events() {
        return null;
    }
    default void itematic$setEvents(ItemEventMap events) {}
    default boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return false;
    }
    default boolean itematic$hasEventListener(ItemEvent event) {
        return false;
    }
    default void itematic$addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type) {}
    default boolean itematic$mayStartUsing(Level world, Player user, InteractionHand hand, ItemStack stack) {
        return true;
    }
}
