package net.errorcraft.itematic.access.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public interface CommandBlockExecutorAccess {
    void itematic$readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup);
    NbtCompound itematic$writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup);
}
