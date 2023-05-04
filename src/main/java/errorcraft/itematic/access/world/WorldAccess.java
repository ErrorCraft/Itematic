package errorcraft.itematic.access.world;

import errorcraft.itematic.item.ItemAccess;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public interface WorldAccess {
    default ItemAccess getItemAccess() {
        return null;
    }

    default RegistryEntry<Item> getItem(RegistryKey<Item> key) {
        return null;
    }
}
