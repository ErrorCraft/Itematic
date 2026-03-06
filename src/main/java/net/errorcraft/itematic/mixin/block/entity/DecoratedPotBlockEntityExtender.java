package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.block.entity.SherdsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityExtender extends BlockEntity {
    public DecoratedPotBlockEntityExtender(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/Sherds;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/block/entity/Sherds;"
        )
    )
    private Sherds fromNbtUseDynamicRegistry(NbtCompound nbt, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return SherdsUtil.fromNbt(nbt, lookup);
    }

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/Sherds;toNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound toNbtUseDynamicRegistry(Sherds instance, NbtCompound nbt, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return instance.itematic$toNbt(nbt, lookup);
    }
}
