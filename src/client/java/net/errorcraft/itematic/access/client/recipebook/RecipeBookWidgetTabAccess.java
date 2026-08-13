package net.errorcraft.itematic.access.client.recipebook;

import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public interface RecipeBookWidgetTabAccess {
    ItemStack itematic$primaryIconItem(ItemAccess items);
    Optional<ItemStack> itematic$secondaryIconItem(ItemAccess items);
}
