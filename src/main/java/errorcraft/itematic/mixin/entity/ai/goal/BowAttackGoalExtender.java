package errorcraft.itematic.mixin.entity.ai.goal;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowAttackGoal.class)
public class BowAttackGoalExtender {
    @Redirect(
        method = "isHoldingBow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/HostileEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingBowIsHoldingUseRegistryKeyCheck(HostileEntity instance, Item item) {
        return instance.isHolding(ItemKeys.BOW);
    }
}
