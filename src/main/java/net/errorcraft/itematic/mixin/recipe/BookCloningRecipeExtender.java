package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.RecipeRemainderItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.recipe.BookCloningRecipe;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BookCloningRecipe.class)
public class BookCloningRecipeExtender {
    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForWrittenBookUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WRITTEN_BOOK);
    }

    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;WRITABLE_BOOK:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForWritableBookUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WRITABLE_BOOK);
    }

    @Redirect(
        method = "getRemainder(Lnet/minecraft/inventory/RecipeInputInventory;)Lnet/minecraft/util/collection/DefaultedList;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForRemainderUseItemComponent(ItemConvertible item, @Local ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.RECIPE_REMAINDER)
            .map(RecipeRemainderItemComponent::item)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }

    @ModifyConstant(
        method = "getRemainder(Lnet/minecraft/inventory/RecipeInputInventory;)Lnet/minecraft/util/collection/DefaultedList;",
        constant = @Constant(
            classValue = WrittenBookItem.class
        )
    )
    private boolean instanceOfWrittenBookItemUseItemComponentCheck(Object reference, Class<WrittenBookItem> clazz, @Local ItemStack inputStack) {
        return inputStack.itematic$hasComponent(ItemComponentTypes.RECIPE_REMAINDER);
    }
}
