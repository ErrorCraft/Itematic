package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BannerPatternHolderItemComponent;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BannerDuplicateRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(BannerDuplicateRecipe.class)
public class BannerDuplicateRecipeExtender {
    @ModifyConstant(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        constant = @Constant(
            classValue = BannerItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBannerItemUseItemComponentCheck(Object reference, Class<BannerItem> clazz, @Local ItemStack inputStack, @Share("dyeColor") LocalRef<DyeColor> dyeColor) {
        Optional<DyeColor> optionalDyeColor = inputStack.itematic$getBehavior(ItemComponentTypes.BANNER_PATTERN_HOLDER)
            .flatMap(BannerPatternHolderItemComponent::color);
        optionalDyeColor.ifPresent(dyeColor::set);
        return optionalDyeColor.isPresent();
    }

    @ModifyVariable(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToBannerItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BannerItem;getColor()Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor getColorUseItemComponent(BannerItem instance, @Share("dyeColor") LocalRef<DyeColor> dyeColor) {
        return dyeColor.get();
    }
}
