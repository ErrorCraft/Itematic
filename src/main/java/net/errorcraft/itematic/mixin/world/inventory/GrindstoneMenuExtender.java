package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuExtender {
    @WrapOperation(
        method = "removeNonCursesFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isEnchantedBookUseDataComponent(ItemStack instance, Object object, Operation<Boolean> original, @Share("grindResultItem") LocalRef<Holder<Item>> grindResultItemReference) {
        Holder<Item> grindResultItem = instance.get(ItematicDataComponents.GRIND_RESULT_ITEM);
        if (grindResultItem == null) {
            return false;
        }

        grindResultItemReference.set(grindResultItem);
        return true;
    }

    @WrapOperation(
        method = "removeNonCursesFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForBookUseDataComponent(ItemStack instance, ItemLike newItem, Operation<ItemStack> original, @Share("grindResultItem") LocalRef<Holder<Item>> grindResultItemReference) {
        return instance.itematic$transmuteCopy(grindResultItemReference.get());
    }
}
