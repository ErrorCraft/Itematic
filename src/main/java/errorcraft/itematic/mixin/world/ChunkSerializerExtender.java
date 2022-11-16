package errorcraft.itematic.mixin.world;

import errorcraft.itematic.block.entity.BlockEntityUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.WorldChunk;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerExtender {
    @Inject(
        method = "method_39797",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/entity/BlockEntity;createFromNbt(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/block/entity/BlockEntity;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        remap = false
    )
    private static void passWorldForReadingNbt(NbtList nbtList, ServerWorld serverWorld, NbtList nbtList2, WorldChunk chunk, CallbackInfo info, int i, NbtCompound nbtCompound, boolean bl, BlockPos blockPos, BlockEntity blockEntity) {
        BlockEntityUtil.readNbt(blockEntity, serverWorld, nbtCompound);
    }
}
