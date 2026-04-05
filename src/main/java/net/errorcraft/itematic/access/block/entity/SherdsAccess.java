package net.errorcraft.itematic.access.block.entity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Optional;

public interface SherdsAccess {
    default NbtCompound itematic$toNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        return null;
    }
    default List<Optional<RegistryEntry<Item>>> itematic$optionalEntries() {
        return null;
    }
    default List<RegistryEntry<Item>> itematic$entries(RegistryWrapper.WrapperLookup lookup) {
        return null;
    }
}
