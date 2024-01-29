package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.access.block.entity.BlockEntityAccess;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockEntity.class)
public abstract class BlockEntityExtender implements BlockEntityAccess {
    @Shadow
    public abstract void readNbt(NbtCompound nbt);

    @Shadow
    @Nullable
    public abstract World getWorld();

    @Unique
    private DynamicRegistryManager registryManager;

    @Redirect(
        method = "method_17897",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
        ),
        remap = false
    )
    private static void removeOriginalReadNbtMethod(BlockEntity instance, NbtCompound nbt) {}

    @Override
    public void itematic$readNbt(NbtCompound nbt, DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
        this.readNbt(nbt);
        this.registryManager = null;
    }

    @Override
    public DynamicRegistryManager itematic$getRegistryManager() {
        World world = this.getWorld();
        if (world == null) {
            return this.registryManager;
        }
        return world.getRegistryManager();
    }
}
