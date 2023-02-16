package errorcraft.itematic.access.item;

import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;

import java.util.Optional;

public interface ItemStackAccess {
    default Optional<NbtCompound> getOptionalNbt() { return Optional.empty(); }
    default boolean isOf(RegistryKey<Item> key) {
        return false;
    }
}
