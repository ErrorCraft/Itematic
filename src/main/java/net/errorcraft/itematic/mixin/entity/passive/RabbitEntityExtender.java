package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RabbitEntity.class)
public abstract class RabbitEntityExtender extends MobEntityExtender {
    protected RabbitEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/entity/mob/PathAwareEntity;DLnet/minecraft/recipe/Ingredient;Z)Lnet/minecraft/entity/ai/goal/TemptGoal;"
        )
    )
    private TemptGoal newTemptGoalSetItems(TemptGoal original) {
        original.itematic$setItems(ItematicItemTags.RABBIT_TEMPT_ITEMS);
        return original;
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/RabbitEntity;isTempting(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(ItemStack stack) {
        return stack.isIn(ItematicItemTags.RABBIT_FOOD);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.RABBIT_SPAWN_EGG;
    }
}
