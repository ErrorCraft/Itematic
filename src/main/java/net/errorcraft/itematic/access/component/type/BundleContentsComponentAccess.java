package net.errorcraft.itematic.access.component.type;

import net.errorcraft.itematic.component.type.BundleContentsComponentExtraFields;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface BundleContentsComponentAccess {
    default List<ItemStack> itematic$stacks() {
        return null;
    }
    default BundleContentsComponentExtraFields itematic$extraFields() {
        return null;
    }
    default void itematic$setExtraFields(BundleContentsComponentExtraFields extraFields) {}
    default void itematic$calculateOccupancy() {}
}
