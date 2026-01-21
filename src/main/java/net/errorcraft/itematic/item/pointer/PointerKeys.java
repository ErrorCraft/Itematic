package net.errorcraft.itematic.item.pointer;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class PointerKeys {
    public static final RegistryKey<Pointer> SPAWN_LOCATION = of("spawn_location");
    public static final RegistryKey<Pointer> LAST_DEATH = of("last_death");

    private PointerKeys() {}

    private static RegistryKey<Pointer> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.POINTER, Identifier.ofVanilla(id));
    }
}
