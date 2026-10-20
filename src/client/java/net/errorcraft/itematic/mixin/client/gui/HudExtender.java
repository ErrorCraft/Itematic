package net.errorcraft.itematic.mixin.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class HudExtender {
    @Shadow
    private ItemStack lastToolHighlight;

    @WrapOperation(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isItemCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.itematic$key() == this.lastToolHighlight.itematic$key();
    }
}
