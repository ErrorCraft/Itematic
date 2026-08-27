package net.errorcraft.itematic.mixin.world.level.storage.loot.functions;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SetStewEffectFunction.class)
public class SetStewEffectFunctionExtender {
    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isSuspiciousStewCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.SUSPICIOUS_STEW);
    }
}
