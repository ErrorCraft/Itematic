package net.errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.Optional;
import java.util.stream.Stream;

public class ItemAccess {
    private final Registry<Item> registry;

    public ItemAccess(DynamicRegistryManager registryManager) {
        this.registry = registryManager.getOrThrow(RegistryKeys.ITEM);
    }

    public RegistryEntry.Reference<Item> getEntry(RegistryKey<Item> key) {
        return this.registry.getOrThrow(key);
    }

    public Optional<RegistryEntry.Reference<Item>> getOptionalEntry(RegistryKey<Item> key) {
        return this.registry.getOptional(key);
    }

    public Stream<RegistryEntry.Reference<Item>> streamEntries() {
        return this.registry.streamEntries();
    }

    public Iterable<RegistryEntry<Item>> iterateEntries(TagKey<Item> tag) {
        return this.registry.iterateEntries(tag);
    }
}
