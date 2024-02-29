package net.errorcraft.itematic.access.entity.boss;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public interface CommandBossBarAccess {
    NbtCompound itematic$toNbt(RegistryWrapper.WrapperLookup lookup);
}
