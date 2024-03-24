package net.errorcraft.itematic.mixin.entity.ai.goal;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.goal.WolfBegGoal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WolfBegGoal.class)
public class WolfBegGoalExtender {
    @Redirect(
        method = "isAttractive",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBoneUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BONE);
    }
}
