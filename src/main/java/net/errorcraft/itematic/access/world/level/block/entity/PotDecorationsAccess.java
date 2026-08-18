package net.errorcraft.itematic.access.world.level.block.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;

public interface PotDecorationsAccess {
    default List<Optional<Holder<Item>>> itematic$optionalEntries() {
        throw new AssertionError("Implemented via mixin");
    }
    default List<Holder<Item>> itematic$entries(HolderLookup.Provider lookup) {
        throw new AssertionError("Implemented via mixin");
    }
}
