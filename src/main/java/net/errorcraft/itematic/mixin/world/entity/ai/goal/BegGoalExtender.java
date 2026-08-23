package net.errorcraft.itematic.mixin.world.entity.ai.goal;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.ai.goal.BegGoal;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BegGoal.class)
public class BegGoalExtender {
    @Redirect(
        method = "playerHoldingInteresting",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBoneCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.BONE);
    }
}
