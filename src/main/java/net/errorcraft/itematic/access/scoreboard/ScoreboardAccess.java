package net.errorcraft.itematic.access.scoreboard;

import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;

public interface ScoreboardAccess {
    NbtList itematic$toNbt(RegistryWrapper.WrapperLookup lookup);
    void itematic$readNbt(NbtList nbt, RegistryWrapper.WrapperLookup lookup);
}
