package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantmentHolderItemBehavior;
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
public class GrindstoneMenuExtender {
    @Redirect(
        method = "removeNonCursesFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isEnchantedBookUseItemBehavior(ItemStack instance, Object o, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        Optional<Holder<Item>> item = instance.itematic$getBehavior(ItemBehaviorType.ENCHANTMENT_HOLDER)
            .map(EnchantmentHolderItemBehavior::grindingTransformsInto);
        item.ifPresent(transformsInto::set);
        return item.isPresent();
    }

    @Redirect(
        method = "removeNonCursesFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForBookUseHolder(ItemStack instance, ItemLike newItem, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        return instance.itematic$transmuteCopy(transformsInto.get());
    }
}
