package net.errorcraft.itematic.access.item;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvent;

public interface AnimalArmorItemTypeAccess {
    default RegistryEntry<SoundEvent> itematic$breakSound() {
        return null;
    };
    default RegistryEntryList<EntityType<?>> itematic$allowedEntities() {
        return null;
    };
}
