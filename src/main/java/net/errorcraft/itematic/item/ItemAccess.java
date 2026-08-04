package net.errorcraft.itematic.item;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.stream.Stream;

public class ItemAccess {
    private final Registry<Item> registry;

    public ItemAccess(RegistryAccess registryManager) {
        this.registry = registryManager.lookupOrThrow(Registries.ITEM);
    }

    public Holder.Reference<Item> getEntry(ResourceKey<Item> key) {
        return this.registry.getOrThrow(key);
    }

    public Optional<Holder.Reference<Item>> getOptionalEntry(ResourceKey<Item> key) {
        return this.registry.get(key);
    }

    public Stream<Holder.Reference<Item>> streamEntries() {
        return this.registry.listElements();
    }

    public Iterable<Holder<Item>> iterateEntries(TagKey<Item> tag) {
        return this.registry.getTagOrEmpty(tag);
    }
}
