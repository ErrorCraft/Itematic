package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.CompostableItemComponent;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.class)
public class ComposterBlockExtender {
    @Redirect(
        method = "onUseWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemConvertible> instance, Object o, ItemStack stack) {
        return stack.itematic$hasBehavior(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "compost",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private static boolean containsKeyUseItemComponentCheckStatic(Object2FloatMap<ItemConvertible> instance, Object o, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$hasBehavior(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "addToComposter",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F",
            remap = false
        )
    )
    private static float getFloatUseItemComponent(Object2FloatMap<ItemConvertible> instance, Object o, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.COMPOSTABLE)
            .map(CompostableItemComponent::levelIncreaseChance)
            .orElse(0.0f);
    }

    @Redirect(
        method = "emptyFullComposter",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBoneMealUseCreateStack(ItemConvertible item, @Local(argsOnly = true) World world) {
        return world.itematic$createStack(ItemKeys.BONE_MEAL);
    }

    @Redirect(
        method = "getInventory",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBoneMealUseCreateStack(ItemConvertible item, @Local(argsOnly = true) WorldAccess world) {
        return world.itematic$createStack(ItemKeys.BONE_MEAL);
    }

    @Mixin(targets = "net/minecraft/block/ComposterBlock$ComposterInventory")
    public static class ComposterInventoryExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
                remap = false
            )
        )
        private boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemConvertible> instance, Object o, int slot, ItemStack stack) {
            return stack.itematic$hasBehavior(ItemComponentTypes.COMPOSTABLE);
        }
    }

    @Mixin(targets = "net/minecraft/block/ComposterBlock$FullComposterInventory")
    public static class FullComposterInventoryExtender {
        @Redirect(
            method = "canExtract",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean isOfForBoneMealUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.BONE_MEAL);
        }
    }
}
