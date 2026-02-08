package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ArmorDyeRecipe;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(ArmorDyeRecipe.class)
public class ArmorDyeRecipeExtender {
    @Redirect(
        method = {
            "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
            "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForDyeableUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasBehavior(ItemComponentTypes.DYEABLE);
    }

    @ModifyConstant(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemComponentForMatches(Object reference, Class<DyeItem> clazz, @Local ItemStack inputStack, @Share("dye") LocalRef<DyeItemComponent> dye) {
        Optional<DyeItemComponent> optionalDye = inputStack.itematic$getBehavior(ItemComponentTypes.DYE);
        optionalDye.ifPresent(dye::set);
        return optionalDye.isPresent();
    }

    @ModifyConstant(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        constant = @Constant(
            classValue = DyeItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeItemUseItemComponentForCraft(Object reference, Class<DyeItem> clazz, @Local(ordinal = 1) ItemStack inputStack, @Share("dye") LocalRef<DyeItemComponent> dye) {
        Optional<DyeItemComponent> optionalDye = inputStack.itematic$getBehavior(ItemComponentTypes.DYE);
        optionalDye.ifPresent(dye::set);
        return optionalDye.isPresent();
    }

    @ModifyVariable(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToDyeItemUseNull(Item instance) {
        return null;
    }

    @ModifyArg(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    @SuppressWarnings("unchecked")
    private <E> E addDyeItemUseItemComponent(E e, @Share("dye") LocalRef<DyeItemComponent> dye) {
        return (E) DyeItem.byColor(dye.get().color());
    }
}
