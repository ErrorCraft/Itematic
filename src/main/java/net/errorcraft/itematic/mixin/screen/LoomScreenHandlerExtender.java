package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BannerPatternHolderItemComponent;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(LoomMenu.class)
public class LoomScreenHandlerExtender {
    @ModifyExpressionValue(
        method = "quickMoveStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private boolean containsProvidersBannerPatternsDataComponentAlsoCheckItemBehaviorComponent(boolean original, @Local(ordinal = 1) ItemStack slotStack) {
        return original && slotStack.itematic$hasBehavior(ItemComponentTypes.BANNER_PATTERN_HOLDER);
    }

    @ModifyConstant(
        method = "quickMoveStack",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<BannerItem> clazz, @Local(ordinal = 1) ItemStack slotStack, @Share("dye") LocalRef<DyeItemComponent> dye) {
        Optional<DyeItemComponent> optionalDye = slotStack.itematic$getBehavior(ItemComponentTypes.DYE);
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
    private DyeColor getColorUseItemComponent(DyeItem instance, @Share("dye") LocalRef<DyeItemComponent> dye) {
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
        private boolean instanceOfBannerItemUseItemComponent(Object reference, Class<BannerItem> clazz, ItemStack stack) {
            return stack.itematic$getBehavior(ItemComponentTypes.BANNER_PATTERN_HOLDER)
                .map(BannerPatternHolderItemComponent::modifiable)
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
        private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, ItemStack stack) {
            return stack.itematic$hasBehavior(ItemComponentTypes.DYE);
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
        private boolean containsProvidersBannerPatternsDataComponentAlsoCheckItemBehaviorComponent(boolean original, ItemStack stack) {
            return original && stack.itematic$hasBehavior(ItemComponentTypes.BANNER_PATTERN_HOLDER);
        }
    }
}
