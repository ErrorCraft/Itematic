package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
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
                value = "NEW",
                target = "(Lnet/minecraft/entity/mob/PathAwareEntity;DLjava/util/function/Predicate;Z)Lnet/minecraft/entity/ai/goal/TemptGoal;"
            )
        )
    )
    private void doNotAddCarrotOnAStickGoalSelector(GoalSelector instance, int priority, Goal goal) {}

    @Redirect(
        method = "method_58372",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private static boolean isInForPigFoodItemsUsePigTemptItemsItemTagCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.isIn(ItematicItemTags.PIG_TEMPT_ITEMS);
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
