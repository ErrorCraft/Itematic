package net.errorcraft.itematic.access.world.item.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

import java.util.Optional;

public interface EnchantableAccess {
    default Optional<Holder<Item>> itematic$transformsInto() {
        return Optional.empty();
    }
    default void itematic$setTransformsInto(Optional<Holder<Item>> transformsInto) {}
}
