package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemTagsUtil;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RabbitEntity.class)
public class RabbitEntityExtender {
    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/ai/goal/TemptGoal"
        )
    )
    private TemptGoal initGoalsNewTemptGoalSetFoodTag(TemptGoal original) {
        original.setFoodTag(ItemTagsUtil.RABBIT_BREEDING_ITEMS);
        return original;
    }

    /**
     * @author ErrorCraft
     * @reason Uses an item tag check instead of direct items.
     */
    @Overwrite
    private static boolean isTempting(ItemStack stack) {
        return stack.isIn(ItemTagsUtil.RABBIT_BREEDING_ITEMS);
    }
}
