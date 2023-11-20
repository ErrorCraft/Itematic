package net.errorcraft.itematic.item.pointer;

import net.errorcraft.itematic.item.pointer.pointers.LastDeathPointer;
import net.errorcraft.itematic.item.pointer.pointers.SpawnLocationPointer;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class Pointers {
    public static final Pointer SPAWN_LOCATION = register(PointerKeys.SPAWN_LOCATION, new SpawnLocationPointer());
    public static final Pointer LAST_DEATH = register(PointerKeys.LAST_DEATH, new LastDeathPointer());

    public static void init() {}

    private static Pointer register(RegistryKey<Pointer> id, Pointer pointer) {
        return Registry.register(ItematicRegistries.POINTER, id, pointer);
    }
}
