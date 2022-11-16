package errorcraft.itematic.mixin.world.chunk;

import errorcraft.itematic.block.entity.BlockEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldChunk.class)
public class WorldChunkExtender {
    @Shadow
    @Final
    World world;

    @Inject(
        method = "loadBlockEntity",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/entity/BlockEntity;createFromNbt(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/block/entity/BlockEntity;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void passWorldForReadingNbt(BlockPos pos, NbtCompound nbt, CallbackInfoReturnable<BlockEntity> info, BlockEntity blockEntity, BlockState blockState) {
        BlockEntityUtil.readNbt(blockEntity, this.world, nbt);
    }
}
