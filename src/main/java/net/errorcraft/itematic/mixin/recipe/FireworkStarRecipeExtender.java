package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.errorcraft.itematic.item.component.components.FireworkShapeModifierItemComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Map;
import java.util.Optional;

@Mixin(FireworkStarRecipe.class)
public class FireworkStarRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"
        )
    )
    private boolean containsKeyUseItemComponentCheck(Map<Item, FireworkExplosion.Shape> instance, Object o, @Local ItemStack itemStack) {
        return itemStack.itematic$hasBehavior(ItemComponentTypes.FIREWORK_SHAPE_MODIFIER);
    }

    @Redirect(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <V> V getUseItemComponent(Map<Item, FireworkExplosion.Shape> instance, Object o, @Local ItemStack input) {
        return (V) input.itematic$getBehavior(ItemComponentTypes.FIREWORK_SHAPE_MODIFIER)
            .map(FireworkShapeModifierItemComponent::shape)
            .orElse(null);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/crafting/FireworkStarRecipe;TWINKLE_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean flickerModifierUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.itematic$isOf(ItemKeys.GLOWSTONE_DUST);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/crafting/FireworkStarRecipe;TRAIL_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean trailModifierUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.itematic$isOf(ItemKeys.DIAMOND);
    }

    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/crafting/FireworkStarRecipe;GUNPOWDER_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean testGunpowderUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.itematic$isOf(ItemKeys.GUNPOWDER);
    }

    @ModifyConstant(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        constant = @Constant(
            classValue = DyeItem.class
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local ItemStack itemStack) {
        return itemStack.itematic$hasBehavior(ItemComponentTypes.DYE);
    }

    @ModifyConstant(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemComponentCheck(Object reference, Class<DyeItem> clazz, @Local ItemStack ingredient, @Share("dye") LocalRef<DyeItemComponent> dye) {
        Optional<DyeItemComponent> optionalDye = ingredient.itematic$getBehavior(ItemComponentTypes.DYE);
        optionalDye.ifPresent(dye::set);
        return optionalDye.isPresent();
    }

    @Redirect(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/crafting/FireworkStarRecipe;TRAIL_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private Item castToDyeItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/DyeItem;getDyeColor()Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor getColorUseItemComponent(DyeItem instance, @Share("dye") LocalRef<DyeItemComponent> dye) {
        return dye.get().color();
    }

    @Redirect(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkStarUseRegistryEntry(ItemLike item, @Local(argsOnly = true) HolderLookup.Provider lookup) {
        return lookup.lookupOrThrow(Registries.ITEM)
            .get(ItemKeys.FIREWORK_STAR)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
}
