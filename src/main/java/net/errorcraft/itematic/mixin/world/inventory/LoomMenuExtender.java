package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DyeItemBehavior;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(LoomMenu.class)
public class LoomMenuExtender {
    @ModifyExpressionValue(
        method = "quickMoveStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private boolean alsoCheckBannerPatternHolderItemBehavior(boolean original, @Local(ordinal = 1) ItemStack slotStack) {
        return original && slotStack.itematic$hasBehavior(ItemBehaviorType.BANNER_PATTERN_HOLDER);
    }

    @ModifyConstant(
        method = "quickMoveStack",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemBehavior(Object reference, Class<BannerItem> clazz, @Local(ordinal = 1) ItemStack slotStack, @Share("dye") LocalRef<DyeItemBehavior> dye) {
        Optional<DyeItemBehavior> optionalDye = slotStack.itematic$getBehavior(ItemBehaviorType.DYE);
        optionalDye.ifPresent(dye::set);
        return optionalDye.isPresent();
    }

    @Redirect(
        method = "setupResultSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item getItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "setupResultSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/DyeItem;getDyeColor()Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor getDyeColorUseItemBehavior(DyeItem instance, @Share("dye") LocalRef<DyeItemBehavior> dye) {
        return dye.get().color();
    }

    @Mixin(targets = "net/minecraft/world/inventory/LoomMenu$3")
    public static class BannerSlotExtender {
        @ModifyConstant(
            method = "mayPlace",
            constant = @Constant(
                classValue = BannerItem.class,
                ordinal = 0
            )
        )
        private boolean instanceOfBannerItemUseItemBehavior(Object reference, Class<BannerItem> clazz, ItemStack stack) {
            return stack.itematic$getBehavior(ItemBehaviorType.BANNER_PATTERN_HOLDER)
                .map(BannerPatternHolderItemBehavior::modifiable)
                .orElse(false);
        }
    }

    @Mixin(targets = "net/minecraft/world/inventory/LoomMenu$4")
    public static class DyeSlotExtender {
        @ModifyConstant(
            method = "mayPlace",
            constant = @Constant(
                classValue = DyeItem.class,
                ordinal = 0
            )
        )
        private boolean instanceOfDyeItemCheckDyeItemBehavior(Object reference, Class<DyeItem> clazz, ItemStack stack) {
            return stack.itematic$hasBehavior(ItemBehaviorType.DYE);
        }
    }

    @Mixin(targets = "net/minecraft/world/inventory/LoomMenu$5")
    public static class BannerPatternSlotExtender {
        @ModifyExpressionValue(
            method = "mayPlace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
            )
        )
        private boolean alsoCheckBannerPatternHolderItemBehavior(boolean original, ItemStack stack) {
            return original && stack.itematic$hasBehavior(ItemBehaviorType.BANNER_PATTERN_HOLDER);
        }
    }
}
