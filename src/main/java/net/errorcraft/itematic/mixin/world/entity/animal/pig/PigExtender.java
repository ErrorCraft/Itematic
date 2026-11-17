package net.errorcraft.itematic.mixin.world.entity.animal.pig;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.errorcraft.itematic.world.item.component.EntityController;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(Pig.class)
public abstract class PigExtender extends MobExtender {
    protected PigExtender(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @ModifyArg(
        method = "registerGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/TemptGoal;<init>(Lnet/minecraft/world/entity/PathfinderMob;DLjava/util/function/Predicate;Z)V",
            ordinal = 0
        )
    )
    private Predicate<ItemStack> temptCarrotOnAStickUseDataComponent(Predicate<ItemStack> items) {
        return this::isControlItem;
    }

    @WrapOperation(
        method = "lambda$registerGoals$1",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private static boolean isPigFoodItemsCheckTag(ItemStack instance, TagKey<Item> tagKey, Operation<Boolean> original) {
        return instance.is(ItematicItemTags.PIG_TEMPT_ITEMS);
    }

    @WrapOperation(
        method = "getControllingPassenger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCarrotOnAStickUseDataComponent(Player instance, Item item, Operation<Boolean> original) {
        return instance.isHolding(this::isControlItem);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.PIG_SPAWN_EGG;
    }

    @Unique
    private boolean isControlItem(ItemStack stack) {
        EntityController entityController = stack.get(ItematicDataComponents.ENTITY_CONTROLLER);
        return entityController != null && this.is(entityController.target());
    }
}
