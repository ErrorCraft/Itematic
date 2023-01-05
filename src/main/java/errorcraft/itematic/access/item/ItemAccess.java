package errorcraft.itematic.access.item;

import errorcraft.itematic.item.ItemBase;
import errorcraft.itematic.item.component.ItemComponent;

import java.util.Set;

public interface ItemAccess {
    default ItemBase getItemBase() {
        return null;
    }
    default void setItemBase(ItemBase base) {}
    default Set<ItemComponent> getComponents() {
        return null;
    }
    default void setComponents(Set<ItemComponent> components) {}
}
