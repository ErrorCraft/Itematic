package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.ItemBase;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;

import java.util.Optional;

public interface ItemAccess {
    default ItemBase getItemBase() {
        return null;
    }
    default void setItemBase(ItemBase base) {}
    default ItemComponentSet getComponents() {
        return null;
    }
    default void setComponents(ItemComponentSet components) {}
    default <T extends ItemComponent> boolean hasComponent(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent> Optional<T> getComponent(ItemComponentType<T> type) {
        return Optional.empty();
    }
}
