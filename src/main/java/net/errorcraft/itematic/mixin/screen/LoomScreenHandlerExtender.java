package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BannerPatternHolderItemComponent;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(LoomScreenHandler.class)
public class LoomScreenHandlerExtender {
    @ModifyExpressionValue(
        method = "quickMove",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    private boolean containsProvidersBannerPatternsDataComponentAlsoCheckItemBehaviorComponent(boolean original, @Local(ordinal = 1) ItemStack slotStack) {
        return original && slotStack.itematic$hasBehavior(ItemComponentTypes.BANNER_PATTERN_HOLDER);
    }

    @ModifyConstant(
        method = "quickMove",
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
        method = "updateOutputSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item getItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "updateOutputSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeItem;getColor()Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor getColorUseItemComponent(DyeItem instance, @Share("dye") LocalRef<DyeItemComponent> dye) {
        return dye.get().color();
    }

    @Mixin(targets = "net/minecraft/screen/LoomScreenHandler$3")
    public static class BannerSlotExtender {
        @ModifyConstant(
            method = "canInsert",
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

    @Mixin(targets = "net/minecraft/screen/LoomScreenHandler$4")
    public static class DyeSlotExtender {
        @ModifyConstant(
            method = "canInsert",
            constant = @Constant(
                classValue = DyeItem.class,
                ordinal = 0
            )
        )
        private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, ItemStack stack) {
            return stack.itematic$hasBehavior(ItemComponentTypes.DYE);
        }
    }

    @Mixin(targets = "net/minecraft/screen/LoomScreenHandler$5")
    public static class BannerPatternSlotExtender {
        @ModifyExpressionValue(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
            )
        )
        private boolean containsProvidersBannerPatternsDataComponentAlsoCheckItemBehaviorComponent(boolean original, ItemStack stack) {
            return original && stack.itematic$hasBehavior(ItemComponentTypes.BANNER_PATTERN_HOLDER);
        }
    }
}
