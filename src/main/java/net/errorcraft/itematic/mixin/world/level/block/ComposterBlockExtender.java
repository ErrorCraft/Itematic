package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.CompostableItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.class)
public class ComposterBlockExtender {
    @Redirect(
        method = {
            "useItemOn",
            "insertItem"
        },
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z"
        )
    )
    private static boolean containsKeyCheckCompostableItemBehavior(Object2FloatMap<ItemLike> instance, Object o, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return itemStack.itematic$hasBehavior(ItemBehaviorType.COMPOSTABLE);
    }

    @Redirect(
        method = "addItem",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F"
        )
    )
    private static float getFloatUseItemBehavior(Object2FloatMap<ItemLike> instance, Object o, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return itemStack.itematic$getBehavior(ItemBehaviorType.COMPOSTABLE)
            .map(CompostableItemBehavior::levelIncreaseChance)
            .orElse(0.0f);
    }

    @Redirect(
        method = "extractProduce",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBoneMealUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(ItemIds.BONE_MEAL);
    }

    @Redirect(
        method = "getContainer",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBoneMealUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) LevelAccessor level) {
        return level.itematic$createStack(ItemIds.BONE_MEAL);
    }

    @Mixin(targets = "net/minecraft/world/level/block/ComposterBlock$InputContainer")
    public static class InputContainerExtender {
        @Redirect(
            method = "canPlaceItemThroughFace",
            at = @At(
                value = "INVOKE",
                target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z"
            )
        )
        private boolean containsKeyCheckCompostableItemBehavior(Object2FloatMap<ItemLike> instance, Object o, int slot, ItemStack itemStack) {
            return itemStack.itematic$hasBehavior(ItemBehaviorType.COMPOSTABLE);
        }
    }

    @Mixin(targets = "net/minecraft/world/level/block/ComposterBlock$OutputContainer")
    public static class OutputContainerExtender {
        @Redirect(
            method = "canTakeItemThroughFace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
        )
        private boolean isBoneMealCheckId(ItemStack instance, Object o) {
            return instance.is(ItemIds.BONE_MEAL);
        }
    }
}
