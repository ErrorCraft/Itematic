package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BannerPatternHolderItemComponent;
import net.errorcraft.itematic.item.component.components.BannerPatternItemComponent;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(LoomScreenHandler.class)
public class LoomScreenHandlerExtender {
    @ModifyConstant(
        method = "getPatternsFor",
        constant = @Constant(
            classValue = BannerPatternItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBannerPatternItemUseItemComponent(Object reference, Class<BannerPatternItem> clazz, ItemStack stack, @Share("bannerPatternItemComponent") LocalRef<BannerPatternItemComponent> bannerPatternItemComponent) {
        Optional<BannerPatternItemComponent> optionalComponent = stack.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN);
        optionalComponent.ifPresent(bannerPatternItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "getPatternsFor",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToBannerPatternItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "getPatternsFor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BannerPatternItem;getPattern()Lnet/minecraft/registry/tag/TagKey;"
        )
    )
    private TagKey<BannerPattern> getPatternUseItemComponent(BannerPatternItem instance, @Share("bannerPatternItemComponent") LocalRef<BannerPatternItemComponent> bannerPatternItemComponent) {
        return bannerPatternItemComponent.get().patterns();
    }

    @ModifyConstant(
        method = "quickMove",
        constant = @Constant(
            classValue = BannerItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBannerItemUseItemComponent(Object reference, Class<BannerItem> clazz, @Local(ordinal = 1) ItemStack slotStack) {
        return slotStack.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN_HOLDER)
            .map(BannerPatternHolderItemComponent::modifiable)
            .orElse(false);
    }

    @ModifyConstant(
        method = "quickMove",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<BannerItem> clazz, @Local(ordinal = 1) ItemStack slotStack) {
        return slotStack.itematic$hasComponent(ItemComponentTypes.DYE);
    }

    @ModifyConstant(
        method = "quickMove",
        constant = @Constant(
            classValue = BannerPatternItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBannerPatternItemUseItemComponentCheck(Object reference, Class<BannerPatternItem> clazz, @Local(ordinal = 1) ItemStack slotStack) {
        return slotStack.itematic$hasComponent(ItemComponentTypes.BANNER_PATTERN);
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
    private DyeColor getColorUseItemComponent(DyeItem instance, @Local(ordinal = 1) ItemStack dyeStack) {
        return dyeStack.itematic$getComponent(ItemComponentTypes.DYE)
            .map(DyeItemComponent::color)
            .orElse(DyeColor.WHITE);
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
            return stack.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN_HOLDER)
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
            return stack.itematic$hasComponent(ItemComponentTypes.DYE);
        }
    }

    @Mixin(targets = "net/minecraft/screen/LoomScreenHandler$5")
    public static class BannerPatternSlotExtender {
        @ModifyConstant(
            method = "canInsert",
            constant = @Constant(
                classValue = BannerPatternItem.class,
                ordinal = 0
            )
        )
        private boolean instanceOfBannerPatternItemUseItemComponentCheck(Object reference, Class<BannerPatternItem> clazz, ItemStack stack) {
            return stack.itematic$hasComponent(ItemComponentTypes.BANNER_PATTERN);
        }
    }
}
