package net.errorcraft.itematic.access.component.type;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface ChargedProjectilesComponentAccess {
    default boolean itematic$contains(RegistryKey<Item> item) {
        return false;
    }
    default float itematic$getChargedSpeed() {
        return 0.0f;
    }
}
