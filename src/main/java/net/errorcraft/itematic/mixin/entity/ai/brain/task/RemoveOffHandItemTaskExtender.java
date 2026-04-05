package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.ai.brain.task.RemoveOffHandItemTask;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RemoveOffHandItemTask.class)
public class RemoveOffHandItemTaskExtender {
    @ModifyExpressionValue(
        method = "method_47299",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    private static boolean containsBlocksAttacksDataComponentAlsoCheckItemBehaviorComponent(boolean original, @Local(argsOnly = true) PiglinEntity piglin) {
        return original && piglin.getOffHandStack().itematic$hasBehavior(ItemComponentTypes.ATTACK_BLOCKING);
    }
}
