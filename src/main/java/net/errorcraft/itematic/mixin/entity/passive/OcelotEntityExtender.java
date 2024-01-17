package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OcelotEntity.class)
public class OcelotEntityExtender {
    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItematicItemTags.OCELOT_FOOD);
    }

    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/entity/passive/OcelotEntity;DLnet/minecraft/recipe/Ingredient;Z)Lnet/minecraft/entity/passive/OcelotEntity$OcelotTemptGoal;"
        )
    )
    private OcelotEntity.OcelotTemptGoal newTemptGoalSetItems(OcelotEntity.OcelotTemptGoal original) {
        original.itematic$setItems(ItematicItemTags.OCELOT_TEMPT_ITEMS);
        return original;
    }
}
