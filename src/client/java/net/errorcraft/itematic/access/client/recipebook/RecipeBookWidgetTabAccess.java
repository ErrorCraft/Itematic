package net.errorcraft.itematic.access.client.recipebook;

import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface RecipeBookWidgetTabAccess {
    ItemStack itematic$primaryIconItem(ItemAccess items);
    Optional<ItemStack> itematic$secondaryIconItem(ItemAccess items);
}
