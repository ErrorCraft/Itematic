package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PigEntity.class)
public abstract class PigEntityExtender extends AnimalEntity {
    protected PigEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CARROT_ON_A_STICK:Lnet/minecraft/item/Item;"
            )
        )
    )
    private void initGoalsDoNotAddCarrotOnAStickGoalSelector(GoalSelector instance, int priority, Goal goal) {}

    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/ai/goal/TemptGoal",
            ordinal = 1
        )
    )
    private TemptGoal initGoalsNewTemptGoalSetFoodTag(TemptGoal original) {
        original.setFoodTag(ItematicItemTags.PIG_TEMPTING_ITEMS);
        return original;
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isBreedingItemTestUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItematicItemTags.PIG_BREEDING_ITEMS);
    }

    @Redirect(
        method = "onStruckByLightning",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForGoldenSwordUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.GOLDEN_SWORD));
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForSaddleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.SADDLE);
    }

    @Redirect(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/PigEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForSaddleUseRegistryKey(PigEntity instance, ItemConvertible itemConvertible) {
        return this.dropItem(ItemKeys.SADDLE);
    }
}
