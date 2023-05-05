package net.errorcraft.itematic.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockEntityUtil {
    public static void readNbt(@Nullable BlockEntity blockEntity, World world, NbtCompound nbt) {
        if (blockEntity == null) {
            return;
        }
        blockEntity.readNbt(nbt, world.getRegistryManager());
    }
}
