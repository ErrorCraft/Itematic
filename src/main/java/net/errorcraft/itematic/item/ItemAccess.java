package net.errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class ItemAccess {
    private final Registry<Item> registry;

    public ItemAccess(DynamicRegistryManager registryManager) {
        this.registry = registryManager.get(RegistryKeys.ITEM);
    }

    public RegistryEntry.Reference<Item> getEntry(RegistryKey<Item> key) {
        return this.registry.entryOf(key);
    }

    public Optional<RegistryEntry.Reference<Item>> getOptionalEntry(RegistryKey<Item> key) {
        return this.registry.getEntry(key);
    }

    public Optional<RegistryEntry.Reference<Item>> getOptionalEntry(int id) {
        return this.registry.getEntry(id);
    }

    public Item get(int id) {
        return this.registry.get(id);
    }

    public Stream<RegistryEntry.Reference<Item>> streamEntries() {
        return this.registry.streamEntries();
    }

    public Iterable<RegistryEntry<Item>> iterateEntries(TagKey<Item> tag) {
        return this.registry.iterateEntries(tag);
    }

    public Set<Identifier> getIds() {
        return this.registry.getIds();
    }
}
