package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.DyeItemBehavior;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.FireworkStarFadeRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(FireworkStarFadeRecipe.class)
public class FireworkStarFadeRecipeExtender {
    @Redirect(
        method = {
            "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean testFireworkStarCheckId(Ingredient instance, ItemStack input) {
        return input.is(ItemIds.FIREWORK_STAR);
    }

    @ModifyConstant(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        constant = @Constant(
            classValue = DyeItem.class
        )
    )
    private boolean instanceOfDyeItemCheckDyeItemBehavior(Object reference, Class<DyeItem> clazz, @Local(name = "itemStack") ItemStack itemStack) {
        return itemStack.itematic$hasBehavior(ItemBehaviorType.DYE);
    }

    @ModifyConstant(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemBehavior(Object reference, Class<DyeItem> clazz, @Local(name = "itemStack") ItemStack itemStack, @Share("dye") LocalRef<DyeItemBehavior> dye) {
        Optional<DyeItemBehavior> optionalDye = itemStack.itematic$getBehavior(ItemBehaviorType.DYE);
        optionalDye.ifPresent(dye::set);
        return optionalDye.isPresent();
    }

    @ModifyVariable(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At("LOAD"),
        name = "item"
    )
    @Nullable
    private Item castToDyeItemUseNull(Item item) {
        return null;
    }

    @Redirect(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/DyeItem;getDyeColor()Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor getDyeColorUseItemBehavior(DyeItem instance, @Share("dye") LocalRef<DyeItemBehavior> dye) {
        return dye.get().color();
    }
}
