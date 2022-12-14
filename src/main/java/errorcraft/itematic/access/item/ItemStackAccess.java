package errorcraft.itematic.access.item;

import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public interface ItemStackAccess {
    default Optional<NbtCompound> getOptionalNbt() { return Optional.empty(); }
}
