package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.errorcraft.itematic.item.component.components.FireworkShapeModifierItemComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.FireworkStarRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Map;
import java.util.Optional;

@Mixin(FireworkStarRecipe.class)
public class FireworkStarRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        )
    )
    private boolean matchesTestUseItemComponentCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.hasComponent(ItemComponentTypes.FIREWORK_SHAPE_MODIFIER);
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        )
    )
    private boolean craftTestUseItemComponentCheck(Ingredient instance, ItemStack itemStack, @Share("fireworkTypeModifierItemComponent") LocalRef<FireworkShapeModifierItemComponent> fireworkTypeModifierItemComponent) {
        Optional<FireworkShapeModifierItemComponent> optionalComponent = itemStack.getComponent(ItemComponentTypes.FIREWORK_SHAPE_MODIFIER);
        optionalComponent.ifPresent(fireworkTypeModifierItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <K, V> V craftGetUseItemComponent(Map<K, V> instance, Object o, @Share("fireworkTypeModifierItemComponent") LocalRef<FireworkShapeModifierItemComponent> fireworkTypeModifierItemComponent) {
        return (V) fireworkTypeModifierItemComponent.get().shape();
    }

    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/recipe/FireworkStarRecipe;FLICKER_MODIFIER:Lnet/minecraft/recipe/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean flickerModifierUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isOf(ItemKeys.GLOWSTONE_DUST);
    }

    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/recipe/FireworkStarRecipe;TRAIL_MODIFIER:Lnet/minecraft/recipe/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean trailModifierUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isOf(ItemKeys.DIAMOND);
    }

    @Redirect(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/recipe/FireworkStarRecipe;GUNPOWDER:Lnet/minecraft/recipe/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean matchesTestGunpowderUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isOf(ItemKeys.GUNPOWDER);
    }

    @ModifyConstant(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        constant = @Constant(classValue = DyeItem.class)
    )
    private boolean matchesInstanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local ItemStack itemStack) {
        return itemStack.hasComponent(ItemComponentTypes.DYE);
    }

    @ModifyConstant(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean craftInstanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local(ordinal = 1) ItemStack ingredient, @Share("dyeItemComponent") LocalRef<DyeItemComponent> dyeItemComponent) {
        Optional<DyeItemComponent> optionalComponent = ingredient.getComponent(ItemComponentTypes.DYE);
        optionalComponent.ifPresent(dyeItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 1
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/recipe/FireworkStarRecipe;TRAIL_MODIFIER:Lnet/minecraft/recipe/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private Item craftCastToDyeItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeItem;getColor()Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor craftGetColorUseItemComponent(DyeItem instance, @Share("dyeItemComponent") LocalRef<DyeItemComponent> dyeItemComponent) {
        return dyeItemComponent.get().color();
    }

    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack craftNewItemStackUseRegistryEntry(ItemConvertible item, @Local DynamicRegistryManager dynamicRegistryManager) {
        return new ItemStack(dynamicRegistryManager.get(RegistryKeys.ITEM).entryOf(ItemKeys.FIREWORK_STAR));
    }

    @Redirect(
        method = "getOutput",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getOutputNewItemStackUseRegistryEntry(ItemConvertible item, DynamicRegistryManager registryManager) {
        return new ItemStack(registryManager.get(RegistryKeys.ITEM).entryOf(ItemKeys.FIREWORK_STAR));
    }
}
