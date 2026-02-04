package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.errorcraft.itematic.item.component.components.FireworkShapeModifierItemComponent;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.FireworkStarRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.DyeColor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Map;
import java.util.Optional;

@Mixin(FireworkStarRecipe.class)
public class FireworkStarRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"
        )
    )
    private boolean containsKeyUseItemComponentCheck(Map<Item, FireworkExplosionComponent.Type> instance, Object o, @Local ItemStack itemStack) {
        return itemStack.itematic$hasBehavior(ItemComponentTypes.FIREWORK_SHAPE_MODIFIER);
    }

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <V> V getUseItemComponent(Map<Item, FireworkExplosionComponent.Type> instance, Object o, @Local ItemStack input) {
        return (V) input.itematic$getBehavior(ItemComponentTypes.FIREWORK_SHAPE_MODIFIER)
            .map(FireworkShapeModifierItemComponent::shape)
            .orElse(null);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
            "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"
        },
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
        return itemStack.itematic$isOf(ItemKeys.GLOWSTONE_DUST);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
            "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"
        },
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
        return itemStack.itematic$isOf(ItemKeys.DIAMOND);
    }

    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
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
    private boolean testGunpowderUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.itematic$isOf(ItemKeys.GUNPOWDER);
    }

    @ModifyConstant(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        constant = @Constant(
            classValue = DyeItem.class
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local ItemStack itemStack) {
        return itemStack.itematic$hasBehavior(ItemComponentTypes.DYE);
    }

    @ModifyConstant(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local ItemStack ingredient, @Share("dyeItemComponent") LocalRef<DyeItemComponent> dyeItemComponent) {
        Optional<DyeItemComponent> optionalComponent = ingredient.itematic$getBehavior(ItemComponentTypes.DYE);
        optionalComponent.ifPresent(dyeItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
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
    private Item castToDyeItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeItem;getColor()Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor getColorUseItemComponent(DyeItem instance, @Share("dyeItemComponent") LocalRef<DyeItemComponent> dyeItemComponent) {
        return dyeItemComponent.get().color();
    }

    @Redirect(
        method = {
            "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
            "getResult"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkStarUseRegistryEntry(ItemConvertible item, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return lookup.getOrThrow(RegistryKeys.ITEM)
            .getOptional(ItemKeys.FIREWORK_STAR)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
}
