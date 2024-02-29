package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.access.world.CommandBlockExecutorAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBlockMinecartEntity.class)
public abstract class CommandBlockMinecartEntityExtender extends AbstractMinecartEntity {
    protected CommandBlockMinecartEntityExtender(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "writeCustomDataToNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/CommandBlockExecutor;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound toJsonStringUseDynamicRegistry(CommandBlockExecutor instance, NbtCompound nbt) {
        return ((CommandBlockExecutorAccess) instance).itematic$writeNbt(nbt, this.getWorld().getRegistryManager());
    }

    @Redirect(
        method = "readCustomDataFromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/CommandBlockExecutor;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
        )
    )
    private void fromJsonUseDynamicRegistry(CommandBlockExecutor instance, NbtCompound nbt) {
        ((CommandBlockExecutorAccess) instance).itematic$readNbt(nbt, this.getWorld().getRegistryManager());
    }
}
