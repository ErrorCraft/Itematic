package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.CommandBlockExecutorAccess;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBlockBlockEntity.class)
public class CommandBlockBlockEntityExtender {
    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/CommandBlockExecutor;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound toJsonStringUseDynamicRegistry(CommandBlockExecutor instance, NbtCompound nbt, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return ((CommandBlockExecutorAccess) instance).itematic$writeNbt(nbt, lookup);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/CommandBlockExecutor;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
        )
    )
    private void fromJsonUseDynamicRegistry(CommandBlockExecutor instance, NbtCompound nbt, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        ((CommandBlockExecutorAccess) instance).itematic$readNbt(nbt, lookup);
    }
}
