package net.errorcraft.itematic.mixin.client.player;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public class LocalPlayerExtender {
    @WrapMethod(
        method = "openItemGui"
    )
    private void alsoCheckTextHolderItemBehavior(ItemStack stack, InteractionHand hand, Operation<Void> original) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            return;
        }

        original.call(stack, hand);
    }
}
