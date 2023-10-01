package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SheepEntity.class)
public class SheepEntityExtender {
    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/ai/goal/TemptGoal"
        )
    )
    private TemptGoal initGoalsNewTemptGoalSetFoodTag(TemptGoal original) {
        original.setFoodTag(ItematicItemTags.SHEEP_TEMPTING_ITEMS);
        return original;
    }
}
