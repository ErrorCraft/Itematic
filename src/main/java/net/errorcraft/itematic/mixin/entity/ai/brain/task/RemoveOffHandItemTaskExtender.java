package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.brain.task.RemoveOffHandItemTask;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RemoveOffHandItemTask.class)
public class RemoveOffHandItemTaskExtender {
    @Redirect(
        method = "method_47299",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForShieldUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SHIELD);
    }
}
