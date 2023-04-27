package errorcraft.itematic.access.entity.vehicle;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface BoatEntityAccess {
    default RegistryKey<Item> asItemKey() {
        return ItemKeys.AIR;
    }
}
