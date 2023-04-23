package errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerExtender {
    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 1
        )
    )
    private boolean updateResultIsOfUseRegistryEntryCheck(ItemStack instance, Item item, @Local(ordinal = 2) LocalRef<ItemStack> secondaryInputItem) {
        return instance.itemMatches(secondaryInputItem.get().getRegistryEntry());
    }

    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isDamageable()Z",
            ordinal = 1
        )
    )
    private boolean updateResultIsDamageableAlwaysTrue(ItemStack instance) {
        return true;
    }

    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean updateResultIsOfHoldsEnchantmentsUseComponentCheck(ItemStack instance, Item item) {
        return instance.hasComponent(ItemComponentTypes.ENCHANTMENT_HOLDER);
    }

    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 2
        )
    )
    private boolean updateResultIsOfIsEnchantmentHolderUseComponentCheck(ItemStack instance, Item item) {
        return instance.hasComponent(ItemComponentTypes.ENCHANTMENT_HOLDER);
    }
}
