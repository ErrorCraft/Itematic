package net.errorcraft.itematic.mixin.entity.projectile;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpectralArrowEntity.class)
public abstract class SpectralArrowEntityExtender extends PersistentProjectileEntity {
    protected SpectralArrowEntityExtender(EntityType<? extends PersistentProjectileEntity> type, World world, ItemStack stack) {
        super(type, world, stack);
    }

    @ModifyArg(
        method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private static ItemStack defaultStackUseCreateStack(ItemStack stack, @Local(argsOnly = true) World world) {
        return world.itematic$createStack(ItemKeys.SPECTRAL_ARROW);
    }
}
