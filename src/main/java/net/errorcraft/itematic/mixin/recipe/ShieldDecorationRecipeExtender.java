package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BannerPatternHolderItemComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShieldDecorationRecipe;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeExtender {
    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean paperUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SHIELD);
    }

    @ModifyConstant(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;" },
        constant = @Constant(
            classValue = BannerItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBannerItemUseItemComponent(Object reference, Class<BannerItem> clazz, @Local(ordinal = 2) ItemStack slotStack) {
        return slotStack.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN_HOLDER)
            .map(BannerPatternHolderItemComponent::modifiable)
            .orElse(false);
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=Base"
            )
        )
    )
    private Item getItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BannerItem;getColor()Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor getColorUseItemComponent(BannerItem instance, @Local(ordinal = 0) ItemStack bannerStack) {
        return bannerStack.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN_HOLDER)
            .flatMap(BannerPatternHolderItemComponent::color)
            .orElse(DyeColor.WHITE);
    }
}
