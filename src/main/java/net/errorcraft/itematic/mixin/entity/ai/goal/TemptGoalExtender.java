package net.errorcraft.itematic.mixin.entity.ai.goal;

import net.errorcraft.itematic.access.entity.ai.goal.TemptGoalAccess;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TemptGoal.class)
public class TemptGoalExtender implements TemptGoalAccess {
    @Unique
    private TagKey<Item> items;

    @Redirect(
        method = "isTemptedBy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testTemptItemsUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        if (this.items == null) {
            return false;
        }
        return itemStack.isIn(this.items);
    }

    @Override
    public void itematic$setItems(TagKey<Item> items) {
        this.items = items;
    }
}
