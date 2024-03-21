package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PigEntity.class)
public abstract class PigEntityExtender extends MobEntityExtender {
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
    private void doNotAddCarrotOnAStickGoalSelector(GoalSelector instance, int priority, Goal goal) {}

    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/entity/mob/PathAwareEntity;DLnet/minecraft/recipe/Ingredient;Z)Lnet/minecraft/entity/ai/goal/TemptGoal;",
            ordinal = 1
        )
    )
    private TemptGoal newTemptGoalSetItems(TemptGoal original) {
        original.itematic$setItems(ItematicItemTags.PIG_TEMPT_ITEMS);
        return original;
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItematicItemTags.PIG_FOOD);
    }

    @Redirect(
        method = "onStruckByLightning",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForGoldenSwordUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GOLDEN_SWORD);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForSaddleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SADDLE);
    }

    @Redirect(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/PigEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForSaddleUseRegistryKey(PigEntity instance, ItemConvertible itemConvertible) {
        return this.itematic$dropItem(ItemKeys.SADDLE);
    }

    @Redirect(
        method = "getControllingPassenger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingForCarrotOnAStickUseRegistryKeyCheck(PlayerEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.CARROT_ON_A_STICK);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.PIG_SPAWN_EGG;
    }
}
