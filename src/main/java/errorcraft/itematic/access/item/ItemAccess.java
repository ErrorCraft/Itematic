package errorcraft.itematic.access.item;

import errorcraft.itematic.item.ItemBase;

public interface ItemAccess {
    default ItemBase getItemBase() {
        return null;
    }
    default void setItemBase(ItemBase base) {}
}
