package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemExtender {
    @Inject(
        method = "loadProjectiles",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getAmmunitionUseItemComponent(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> info) {
        if (!crossbow.itematic$hasComponent(ItemComponentTypes.SHOOTER)) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "loadProjectiles",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack getAmmunitionUseItemComponent(LivingEntity instance, ItemStack stack) {
        if (stack.itematic$hasComponent(ItemComponentTypes.SHOOTER)) {
            instance.itematic$getAmmunition(stack);
        }

        return ItemStack.EMPTY;
    }
}
