package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemTagsUtil;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntity.class)
public class CatEntityExtender {
    @Shadow
    private TemptGoal temptGoal;

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isBreedingItemTestUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItemTagsUtil.CAT_BREEDING_ITEMS);
    }

    @Inject(
        method = "initGoals",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/passive/CatEntity;temptGoal:Lnet/minecraft/entity/ai/goal/TemptGoal;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void initGoalsStoreTemptGoalFieldSetFoodTag(CallbackInfo info) {
        this.temptGoal.setFoodTag(ItemTagsUtil.CAT_TEMPTING_ITEMS);
    }
}
