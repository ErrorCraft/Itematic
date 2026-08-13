package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AnvilMenu.class)
public class AnvilScreenHandlerExtender {
    @Redirect(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfUseRegistryEntryCheck(ItemStack instance, Item item, @Local(ordinal = 2) ItemStack secondaryInputItem) {
        return instance.is(secondaryInputItem.getItemHolder());
    }

    @Redirect(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z",
            ordinal = 1
        )
    )
    private boolean isDamageableAlwaysTrue(ItemStack instance) {
        return true;
    }

    @Redirect(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/enchantment/Enchantment;canEnchant(Lnet/minecraft/world/item/ItemStack;)Z"
            )
        )
    )
    private boolean isOfForEnchantedBookUseItemBehaviorCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemBehaviorType.ENCHANTMENT_HOLDER);
    }
}
