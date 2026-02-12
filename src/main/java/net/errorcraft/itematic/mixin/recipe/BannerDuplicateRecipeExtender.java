package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BannerPatternHolderItemComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BannerDuplicateRecipe;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(BannerDuplicateRecipe.class)
public class BannerDuplicateRecipeExtender {
    @ModifyConstant(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        constant = @Constant(
            classValue = BannerItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBannerItemUseItemComponentCheck(Object reference, Class<BannerItem> clazz, @Local(ordinal = 2) ItemStack inputStack, @Share("dyeColor") LocalRef<DyeColor> dyeColor) {
        Optional<DyeColor> optionalDyeColor = inputStack.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN_HOLDER)
            .flatMap(BannerPatternHolderItemComponent::color);
        optionalDyeColor.ifPresent(dyeColor::set);
        return optionalDyeColor.isPresent();
    }

    @ModifyVariable(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToBannerItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BannerItem;getColor()Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor getColorUseItemComponent(BannerItem instance, @Share("dyeColor") LocalRef<DyeColor> dyeColor) {
        return dyeColor.get();
    }
}
