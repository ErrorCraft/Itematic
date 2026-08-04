package net.errorcraft.itematic.access.block.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;

public interface SherdsAccess {
    default List<Optional<Holder<Item>>> itematic$optionalEntries() {
        return null;
    }
    default List<Holder<Item>> itematic$entries(HolderLookup.Provider lookup) {
        return null;
    }
}
