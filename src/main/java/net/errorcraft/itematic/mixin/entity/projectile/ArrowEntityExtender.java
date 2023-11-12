package net.errorcraft.itematic.mixin.entity.projectile;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityExtender extends PersistentProjectileEntity {
    @Shadow
    public abstract void initFromStack(ItemStack stack);

    protected ArrowEntityExtender(EntityType<? extends PersistentProjectileEntity> type, World world, ItemStack stack) {
        super(type, world, stack);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V",
        at = @At("TAIL")
    )
    private void initializeStack(World world, LivingEntity owner, ItemStack stack, CallbackInfo info) {
        this.initFromStack(stack);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
        at = @At("TAIL")
    )
    private void initializeStack(World world, double x, double y, double z, ItemStack stack, CallbackInfo info) {
        this.initFromStack(stack);
    }

    @ModifyArg(
        method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private static ItemStack newArrowEntityDefaultStackUseDynamicRegistry(ItemStack stack, @Local World world) {
        return world.itematic$createStack(ItemKeys.ARROW);
    }

    @Redirect(
        method = "initFromStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForTippedArrowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.TIPPED_ARROW);
    }

    @Redirect(
        method = "initFromStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 1
        )
    )
    private boolean isOfForArrowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ARROW);
    }
}
