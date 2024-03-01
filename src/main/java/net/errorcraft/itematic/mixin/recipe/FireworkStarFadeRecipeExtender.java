package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.FireworkStarFadeRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(FireworkStarFadeRecipe.class)
public class FireworkStarFadeRecipeExtender {
    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean fireworkStarUseRegistryKeyCheck(Ingredient instance, ItemStack stack) {
        return stack.itematic$isOf(ItemKeys.FIREWORK_STAR);
    }

    @ModifyConstant(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        constant = @Constant(
            classValue = DyeItem.class
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local ItemStack inputStack) {
        return inputStack.itematic$hasComponent(ItemComponentTypes.DYE);
    }

    @ModifyConstant(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local(ordinal = 1) ItemStack ingredient, @Share("dyeItemComponent") LocalRef<DyeItemComponent> dyeItemComponent) {
        Optional<DyeItemComponent> optionalComponent = ingredient.itematic$getComponent(ItemComponentTypes.DYE);
        optionalComponent.ifPresent(dyeItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToDyeItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeItem;getColor()Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor getColorUseItemComponent(DyeItem instance, @Share("dyeItemComponent") LocalRef<DyeItemComponent> dyeItemComponent) {
        return dyeItemComponent.get().color();
    }
}
