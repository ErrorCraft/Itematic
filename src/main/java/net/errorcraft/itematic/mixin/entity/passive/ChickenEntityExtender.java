package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemTagsUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityExtender extends AnimalEntity {
    protected ChickenEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/ChickenEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity tickMovementDropItemUseRegistryEntry(ChickenEntity instance, ItemConvertible itemConvertible) {
        return this.dropItem(this.getWorld().getItem(ItemKeys.EGG));
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isBreedingItemTestUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItemTagsUtil.CHICKEN_BREEDING_ITEMS);
    }

    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/ai/goal/TemptGoal"
        )
    )
    private TemptGoal initGoalsNewTemptGoalSetFoodTag(TemptGoal original) {
        original.setFoodTag(ItemTagsUtil.CHICKEN_TEMPTING_ITEMS);
        return original;
    }
}
