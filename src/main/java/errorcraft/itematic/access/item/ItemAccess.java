package errorcraft.itematic.access.item;

import errorcraft.itematic.item.ItemBase;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentSet;

import java.util.Set;

public interface ItemAccess {
    default ItemBase getItemBase() {
        return null;
    }
    default void setItemBase(ItemBase base) {}
    default ItemComponentSet getComponents() {
        return null;
    }
    default void setComponents(ItemComponentSet components) {}
}
