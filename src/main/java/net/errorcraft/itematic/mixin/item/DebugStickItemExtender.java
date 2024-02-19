package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.DebugStickItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugStickItem.class)
public class DebugStickItemExtender {
    @Redirect(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DebugStickItem;getTranslationKey()Ljava/lang/String;"
        )
    )
    private String getTranslationKeyUseItemStackVersion(DebugStickItem instance, @Local(argsOnly = true) ItemStack heldStack) {
        return heldStack.getTranslationKey();
    }
}
