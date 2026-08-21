package net.errorcraft.itematic.mixin.world.entity.animal.pig;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Pig.class)
public abstract class PigExtender extends MobExtender {
    protected PigExtender(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "registerGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/entity/PathfinderMob;DLjava/util/function/Predicate;Z)Lnet/minecraft/world/entity/ai/goal/TemptGoal;"
            )
        )
    )
    private void doNotAddCarrotOnAStickGoalSelector(GoalSelector instance, int priority, Goal goal) {}

    @Redirect(
        method = "method_58372",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private static boolean isPigFoodItemsCheckTag(ItemStack instance, TagKey<Item> tag) {
        return instance.is(ItematicItemTags.PIG_TEMPT_ITEMS);
    }

    @Redirect(
        method = "getControllingPassenger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCarrotOnAStickCheckId(Player instance, Item item) {
        return instance.itematic$isHolding(ItemIds.CARROT_ON_A_STICK);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.PIG_SPAWN_EGG;
    }
}
