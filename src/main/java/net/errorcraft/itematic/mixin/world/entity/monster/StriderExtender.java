package net.errorcraft.itematic.mixin.world.entity.monster;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.world.item.component.EntityController;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(Strider.class)
public abstract class StriderExtender extends MobExtender {
    protected StriderExtender(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @ModifyArg(
        method = "registerGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/TemptGoal;<init>(Lnet/minecraft/world/entity/PathfinderMob;DLjava/util/function/Predicate;Z)V"
        )
    )
    private Predicate<ItemStack> temptWarpedFungusOnAStickUseDataComponent(Predicate<ItemStack> items) {
        return item -> items.test(item) || this.isControlItem(item);
    }

    @WrapOperation(
        method = "getControllingPassenger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingWarpedFungusOnAStickUseDataComponent(Player instance, Item item, Operation<Boolean> original) {
        return instance.isHolding(this::isControlItem);
    }

    @WrapOperation(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForWarpedFungusOnAStickUseCreateStack(ItemLike item, Operation<ItemStack> original, @Local(name = "level", argsOnly = true) ServerLevelAccessor level) {
        return level.itematic$createStack(ItemIds.WARPED_FUNGUS_ON_A_STICK);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.STRIDER_SPAWN_EGG;
    }

    @Unique
    private boolean isControlItem(ItemStack stack) {
        EntityController entityController = stack.get(ItematicDataComponents.ENTITY_CONTROLLER);
        return entityController != null && this.is(entityController.target());
    }
}
