package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.BannerDuplicateRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Optional;

@Mixin(BannerDuplicateRecipe.class)
public abstract class BannerDuplicateRecipeExtender extends CustomRecipe {
    @Shadow
    @Final
    private Ingredient banner;

    @WrapOperation(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item getItemUseNull(ItemStack instance, Operation<Item> original) {
        return null;
    }

    @ModifyConstant(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        constant = @Constant(
            classValue = BannerItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBannerItemUseDataComponent(Object reference, Class<BannerItem> clazz, @Local(name = "itemStack") ItemStack itemStack, @Share("bannerBaseColor") LocalRef<DyeColor> bannerBaseColorReference) {
        DyeColor bannerBaseColor = itemStack.get(ItematicDataComponents.BANNER_BASE_COLOR);
        if (bannerBaseColor == null) {
            return false;
        }

        bannerBaseColorReference.set(bannerBaseColor);
        return true;
    }

    @WrapOperation(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BannerItem;getColor()Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor getColorUseDataComponent(BannerItem instance, Operation<DyeColor> original, @Share("bannerBaseColor") LocalRef<DyeColor> bannerBaseColorReference) {
        return bannerBaseColorReference.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            remainders.set(i, this.remainder(stack));
        }

        return remainders;
    }

    @Unique
    private ItemStack remainder(ItemStack stack) {
        Optional<ItemStackTemplate> explicitBannerRemainder = this.banner.itematic$remainder();
        if (explicitBannerRemainder.isPresent()) {
            return explicitBannerRemainder.get().create();
        }

        if (!stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).layers().isEmpty()) {
            return stack.copyWithCount(1);
        }

        return ItemStack.EMPTY;
    }
}
