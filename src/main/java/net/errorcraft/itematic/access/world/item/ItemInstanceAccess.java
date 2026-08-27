package net.errorcraft.itematic.access.world.item;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;

import java.util.Optional;

public interface ItemInstanceAccess {
    default <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        return false;
    }
    default <T extends ItemBehavior<T>> Optional<T> itematic$getBehavior(ItemBehaviorType<T> type) {
        return Optional.empty();
    }
    default boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return false;
    }
    default boolean itematic$hasEventListener(ItemEvent event) {
        return false;
    }
}
