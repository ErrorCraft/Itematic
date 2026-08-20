package net.errorcraft.itematic.world.level;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class ItemAccess {
    private final Registry<Item> registry;

    public ItemAccess(RegistryAccess registryAccess) {
        this.registry = registryAccess.lookupOrThrow(Registries.ITEM);
    }

    public Holder.Reference<Item> getOrThrow(ResourceKey<Item> item) {
        return this.registry.getOrThrow(item);
    }

    public Optional<Holder.Reference<Item>> get(ResourceKey<Item> item) {
        return this.registry.get(item);
    }

    public Iterable<Holder<Item>> iterateTag(TagKey<Item> tag) {
        return this.registry.getTagOrEmpty(tag);
    }
}
