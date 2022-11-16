package errorcraft.itematic.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class BlockEntityUtil {
    public static void readNbt(BlockEntity blockEntity, World world, NbtCompound nbt) {
        if (blockEntity == null) {
            return;
        }
        if (world != null) {
            blockEntity.setWorld(world);
        }
        blockEntity.readNbt(nbt);
    }
}
