package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.CompostableItemComponent;
import net.minecraft.world.item.Item;
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
        method = "useItemOn",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemLike> instance, Object o, ItemStack stack) {
        return stack.itematic$hasBehavior(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "insertItem",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private static boolean containsKeyUseItemComponentCheckStatic(Object2FloatMap<ItemLike> instance, Object o, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$hasBehavior(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "addItem",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F",
            remap = false
        )
    )
    private static float getFloatUseItemComponent(Object2FloatMap<ItemLike> instance, Object o, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.COMPOSTABLE)
            .map(CompostableItemComponent::levelIncreaseChance)
            .orElse(0.0f);
    }

    @Redirect(
        method = "extractProduce",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBoneMealUseCreateStack(ItemLike item, @Local(argsOnly = true) Level world) {
        return world.itematic$createStack(ItemKeys.BONE_MEAL);
    }

    @Redirect(
        method = "getContainer",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBoneMealUseCreateStack(ItemLike item, @Local(argsOnly = true) LevelAccessor world) {
        return world.itematic$createStack(ItemKeys.BONE_MEAL);
    }

    @Mixin(targets = "net/minecraft/world/level/block/ComposterBlock$InputContainer")
    public static class ComposterInventoryExtender {
        @Redirect(
            method = "canPlaceItemThroughFace",
            at = @At(
                value = "INVOKE",
                target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
                remap = false
            )
        )
        private boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemLike> instance, Object o, int slot, ItemStack stack) {
            return stack.itematic$hasBehavior(ItemComponentTypes.COMPOSTABLE);
        }
    }

    @Mixin(targets = "net/minecraft/world/level/block/ComposterBlock$OutputContainer")
    public static class FullComposterInventoryExtender {
        @Redirect(
            method = "canTakeItemThroughFace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
        )
        private boolean isOfForBoneMealUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.BONE_MEAL);
        }
    }
}
