package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantmentHolderItemComponent;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(GrindstoneMenu.class)
public class GrindstoneScreenHandlerExtender {
    @Redirect(
        method = "removeNonCursesFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForEnchantedBookUseItemComponent(ItemStack instance, Item item, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        Optional<Holder<Item>> optionalItem = instance.itematic$getBehavior(ItemComponentTypes.ENCHANTMENT_HOLDER)
            .map(EnchantmentHolderItemComponent::grindingTransformsInto);
        optionalItem.ifPresent(transformsInto::set);
        return optionalItem.isPresent();
    }

    @Redirect(
        method = "removeNonCursesFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack withItemForBookUseItemComponent(ItemStack instance, ItemLike item, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        return instance.itematic$copyWithItem(transformsInto.get());
    }

    @Mixin(targets = {
        "net/minecraft/world/inventory/GrindstoneMenu$2",
        "net/minecraft/world/inventory/GrindstoneMenu$3"
    })
    public static class EnchantedItemSlotExtender {
        @Redirect(
            method = "mayPlace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"
            )
        )
        private boolean isDamageableUseItemComponentCheck(ItemStack instance) {
            return instance.itematic$hasBehavior(ItemComponentTypes.ENCHANTABLE);
        }
    }
}
