package errorcraft.itematic.access.advancement;

import net.minecraft.item.ItemStack;

public interface AdvancementDisplayAccess {
    default void setIcon(ItemStack itemStack) {}
}
