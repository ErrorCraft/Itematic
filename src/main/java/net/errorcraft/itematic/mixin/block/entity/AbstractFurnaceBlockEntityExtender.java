package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FuelItemComponent;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityExtender {
    @Inject(
        method = "getFuelTime",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        ),
        cancellable = true
    )
    public void getFuelTimeUseItemComponent(ItemStack fuel, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(fuel.getComponent(ItemComponentTypes.FUEL).map(FuelItemComponent::ticks).orElse(0));
    }

    @Inject(
        method = "canUseAsFuel",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void canUseAsFuelUseItemComponentCheck(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(stack.hasComponent(ItemComponentTypes.FUEL));
    }
}
