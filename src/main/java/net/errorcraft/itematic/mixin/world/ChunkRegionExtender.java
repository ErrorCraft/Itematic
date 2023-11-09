package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.block.entity.BlockEntityUtil;
import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkRegion.class)
public class ChunkRegionExtender implements WorldViewAccess {
    @Shadow
    @Final
    private ServerWorld world;

    @Inject(
        method = "getBlockEntity",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/entity/BlockEntity;createFromNbt(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/block/entity/BlockEntity;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void passWorldForReadingNbt(BlockPos pos, CallbackInfoReturnable<BlockEntity> info, Chunk chunk, BlockEntity blockEntity, NbtCompound nbtCompound, BlockState blockState) {
        BlockEntityUtil.readNbt(blockEntity, this.world, nbtCompound);
    }

    @Override
    public ItemAccess itematic$getItemAccess() {
        return this.world.itematic$getItemAccess();
    }

    @Override
    public RegistryEntry<Item> itematic$getItem(RegistryKey<Item> key) {
        return this.world.itematic$getItem(key);
    }
}
