package net.errorcraft.itematic.mixin.block.dispenser;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileDispenserBehavior.class)
public class ProjectileDispenserBehaviorExtender extends ItemDispenserBehavior {
    @Inject(
        method = "dispenseSilently",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/dispenser/ProjectileDispenserBehavior;createProjectile(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Position;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/ProjectileEntity;",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void dispenseSilentlyProjectileEntityCheckNull(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> info, @Local ProjectileEntity projectileEntity) {
        if (projectileEntity == null) {
            info.setReturnValue(super.dispenseSilently(pointer, stack));
        }
    }
}
