package net.errorcraft.itematic.mixin.entity.ai.goal;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StepAndDestroyBlockGoal.class)
public class StepAndDestroyBlockGoalExtender {
    @Redirect(
        method = "tick",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack tickNewItemStackUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.EGG));
    }
}
