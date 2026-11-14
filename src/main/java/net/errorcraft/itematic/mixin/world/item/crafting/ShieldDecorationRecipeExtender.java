package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ShieldDecorationRecipe.class)
public abstract class ShieldDecorationRecipeExtender extends CustomRecipe {
    @Shadow
    @Final
    private Ingredient banner;

    @Shadow
    @Final
    private Ingredient target;

    @WrapOperation(
        method = {
            "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;"
        },
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
            classValue = BannerItem.class
        )
    )
    private boolean instanceOfBannerItemCheckBannerBaseColorDataComponent(Object reference, Class<BannerItem> clazz, @Local(name = "itemStack") ItemStack itemStack) {
        return itemStack.has(ItematicDataComponents.BANNER_BASE_COLOR);
    }

    @ModifyConstant(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
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
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
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
        boolean foundPatternBanner = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            final int index = i;
            if (!foundPatternBanner && this.banner.test(stack) && stack.getItem() instanceof BannerItem) {
                foundPatternBanner = true;
                this.banner.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            this.target.itematic$remainder()
                .map(ItemStackTemplate::create)
                .ifPresent(remainder -> remainders.set(index, remainder));
        }

        return remainders;
    }
}
