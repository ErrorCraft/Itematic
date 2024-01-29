package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.block.entity.DecoratedPotBlockEntityUtil;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingDecoratedPotRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingDecoratedPotRecipe.class)
public class CraftingDecoratedPotRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForDecoratedPotIngredientsUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return !instance.isEmpty() && instance.itematic$hasComponent(ItemComponentTypes.DECORATED_POT_PATTERN);
    }

    @Redirect(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForAirUseIsEmptyCheck(ItemStack instance, Item item) {
        return instance.isEmpty();
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry for data-driven items.
     */
    @Overwrite
    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        DecoratedPotBlockEntityUtil.Sherds sherds = new DecoratedPotBlockEntityUtil.Sherds(
            recipeInputInventory.getStack(1).getRegistryEntry(),
            recipeInputInventory.getStack(3).getRegistryEntry(),
            recipeInputInventory.getStack(5).getRegistryEntry(),
            recipeInputInventory.getStack(7).getRegistryEntry()
        );
        return DecoratedPotBlockEntityUtil.createStack(dynamicRegistryManager, sherds);
    }
}
