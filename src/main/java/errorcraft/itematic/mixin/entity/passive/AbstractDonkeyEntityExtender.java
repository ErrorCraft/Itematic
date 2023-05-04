package errorcraft.itematic.mixin.entity.passive;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractDonkeyEntity.class)
public class AbstractDonkeyEntityExtender {
    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean interactMobIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.CHEST);
    }

    @Mixin(targets = "net/minecraft/entity/passive/AbstractDonkeyEntity$1")
    public static class StackReferenceExtender {
        @Shadow(remap = false)
        @Final
        AbstractDonkeyEntity field_27867;

        @Redirect(
            method = "get",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack"
            )
        )
        private ItemStack getNewItemStackUseRegistryEntry(ItemConvertible item) {
            return new ItemStack(this.field_27867.getWorld().getItem(ItemKeys.CHEST));
        }

        @Redirect(
            method = "set",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean setIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.isOf(ItemKeys.CHEST);
        }
    }
}
