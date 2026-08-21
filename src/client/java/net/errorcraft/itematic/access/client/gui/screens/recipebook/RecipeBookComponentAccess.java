package net.errorcraft.itematic.access.client.gui.screens.recipebook;

import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public interface RecipeBookComponentAccess {
    interface TabInfoAccess {
        default ItemStack itematic$primaryIconItem(ItemAccess items) {
            return ItemStack.EMPTY;
        }
        default Optional<ItemStack> itematic$secondaryIconItem(ItemAccess items) {
            return Optional.empty();
        }
    }
}
