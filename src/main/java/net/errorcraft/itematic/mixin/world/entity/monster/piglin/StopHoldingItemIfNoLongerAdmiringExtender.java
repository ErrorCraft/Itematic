package net.errorcraft.itematic.mixin.world.entity.monster.piglin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.StopHoldingItemIfNoLongerAdmiring;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StopHoldingItemIfNoLongerAdmiring.class)
public class StopHoldingItemIfNoLongerAdmiringExtender {
    @ModifyExpressionValue(
        method = "method_47299",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private static boolean alsoCheckItemBehavior(boolean original, @Local(argsOnly = true) Piglin piglin) {
        return original && piglin.getOffhandItem().itematic$hasBehavior(ItemBehaviorType.ATTACK_BLOCKING);
    }
}
