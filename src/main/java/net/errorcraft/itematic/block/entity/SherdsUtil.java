package net.errorcraft.itematic.block.entity;

import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;

public class SherdsUtil {
    private SherdsUtil() {}

    public static Sherds fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        if (nbt == null || !nbt.contains(DecoratedPotBlockEntity.SHERDS_NBT_KEY)) {
            return Sherds.DEFAULT;
        }

        return Sherds.CODEC.parse(lookup.getOps(NbtOps.INSTANCE), nbt.get(DecoratedPotBlockEntity.SHERDS_NBT_KEY))
            .result()
            .orElse(Sherds.DEFAULT);
    }
}
