package errorcraft.itematic.access.entity;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface LivingEntityAccess {
    default boolean isHolding(RegistryKey<Item> key) {
        return false;
    }
}
