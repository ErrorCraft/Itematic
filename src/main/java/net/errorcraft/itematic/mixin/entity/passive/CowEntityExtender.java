package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.CowEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CowEntity.class)
public class CowEntityExtender {
    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/entity/mob/PathAwareEntity;DLnet/minecraft/recipe/Ingredient;Z)Lnet/minecraft/entity/ai/goal/TemptGoal;"
        )
    )
    private TemptGoal newTemptGoalSetItems(TemptGoal original) {
        original.itematic$setItems(ItematicItemTags.COW_TEMPT_ITEMS);
        return original;
    }
}
