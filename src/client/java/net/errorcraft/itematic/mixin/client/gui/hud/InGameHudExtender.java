package net.errorcraft.itematic.mixin.client.gui.hud;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class InGameHudExtender {
    @ModifyExpressionValue(
        method = "renderMiscOverlays",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean checkPresenceEquipmentBehavior(boolean original, @Local ItemStack stack) {
        return original && stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT);
    }
}
