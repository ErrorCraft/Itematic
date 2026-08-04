package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.StopHoldingItemIfNoLongerAdmiring;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StopHoldingItemIfNoLongerAdmiring.class)
public class RemoveOffHandItemTaskExtender {
    @ModifyExpressionValue(
        method = "method_47299",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private static boolean containsBlocksAttacksDataComponentAlsoCheckItemBehaviorComponent(boolean original, @Local(argsOnly = true) Piglin piglin) {
        return original && piglin.getOffhandItem().itematic$hasBehavior(ItemComponentTypes.ATTACK_BLOCKING);
    }
}
