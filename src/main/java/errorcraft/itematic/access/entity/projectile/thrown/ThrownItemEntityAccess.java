package errorcraft.itematic.access.entity.projectile.thrown;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface ThrownItemEntityAccess {
    default RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.AIR;
    }
}
