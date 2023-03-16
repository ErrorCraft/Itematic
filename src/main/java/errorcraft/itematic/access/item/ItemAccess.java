package errorcraft.itematic.access.item;

import errorcraft.itematic.item.ItemBase;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentSet;
import errorcraft.itematic.item.component.ItemComponentType;

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
