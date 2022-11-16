package errorcraft.itematic.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
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

//    public static BlockEntity createFromNbt(BlockPos pos, BlockState blockState, World world, NbtCompound nbt) {
//        BlockEntity blockEntity = BlockEntity.createFromNbt(pos, blockState, nbt);
//        if (blockEntity == null) {
//            return null;
//        }
//        if (world != null) {
//            blockEntity.setWorld(world);
//        }
//        blockEntity.readNbt(nbt);
//        return blockEntity;
//    }
}
