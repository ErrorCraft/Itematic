package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.ItemBase;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public interface ItemAccess {
    default ItemBase itemBase() {
        return null;
    }
    default void setItemBase(ItemBase base) {}
    default ItemComponentSet components() {
        return null;
    }
    default void setComponents(ItemComponentSet components) {}
    default <T extends ItemComponent> boolean hasComponent(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent> Optional<T> getComponent(ItemComponentType<T> type) {
        return Optional.empty();
    }
    default ItemEventMap events() {
        return null;
    }
    default void setEvents(ItemEventMap events) {}
    default void invokeEvent(ItemEvent event, ActionContext.Builder builder) {}
    default boolean mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return true;
    }
}
