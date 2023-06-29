package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemTagsUtil;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PigEntity.class)
public class PigEntityExtender {
    @Redirect(
        method = "initGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CARROT_ON_A_STICK:Lnet/minecraft/item/Item;"
            )
        )
    )
    private void initGoalsDoNotAddCarrotOnAStickGoalSelector(GoalSelector instance, int priority, Goal goal) {}

    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/ai/goal/TemptGoal",
            ordinal = 1
        )
    )
    private TemptGoal initGoalsNewTemptGoalSetFoodTag(TemptGoal original) {
        original.setFoodTag(ItemTagsUtil.PIG_TEMPTING_ITEMS);
        return original;
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isBreedingItemTestUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItemTagsUtil.PIG_BREEDING_ITEMS);
    }
}
