package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemTagsUtil;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OcelotEntity.class)
public class OcelotEntityExtender {
    @Shadow
    private OcelotEntity.OcelotTemptGoal temptGoal;

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isBreedingItemTestUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItemTagsUtil.OCELOT_BREEDING_ITEMS);
    }

    @Inject(
        method = "initGoals",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/passive/OcelotEntity;temptGoal:Lnet/minecraft/entity/passive/OcelotEntity$OcelotTemptGoal;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void initGoalsStoreTemptGoalFieldSetFoodTag(CallbackInfo info) {
        this.temptGoal.setFoodTag(ItemTagsUtil.OCELOT_TEMPTING_ITEMS);
    }
}
