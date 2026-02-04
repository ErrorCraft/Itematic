package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantmentHolderItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerExtender {
    @Redirect(
        method = "grind",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForEnchantedBookUseItemComponent(ItemStack instance, Item item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        Optional<RegistryEntry<Item>> optionalItem = instance.itematic$getBehavior(ItemComponentTypes.ENCHANTMENT_HOLDER)
            .map(EnchantmentHolderItemComponent::grindingTransformsInto);
        optionalItem.ifPresent(transformsInto::set);
        return optionalItem.isPresent();
    }

    @Redirect(
        method = "grind",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;withItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack withItemForBookUseItemComponent(ItemStack instance, ItemConvertible item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        return instance.itematic$copyWithItem(transformsInto.get());
    }

    @Mixin(targets = {
        "net/minecraft/screen/GrindstoneScreenHandler$2",
        "net/minecraft/screen/GrindstoneScreenHandler$3"
    })
    public static class EnchantedItemSlotExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"
            )
        )
        private boolean isDamageableUseItemComponentCheck(ItemStack instance) {
            return instance.itematic$hasBehavior(ItemComponentTypes.ENCHANTABLE);
        }
    }
}
