package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.CompostableItemComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.class)
public class ComposterBlockExtender {
    @Redirect(
        method = "onUse",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemConvertible> instance, Object o, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        return player.getStackInHand(hand).itematic$hasComponent(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "compost",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private static boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemConvertible> instance, Object o, Entity user, BlockState state, ServerWorld world, ItemStack stack) {
        return stack.itematic$hasComponent(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "addToComposter",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F",
            remap = false
        )
    )
    private static float getFloatUseItemComponent(Object2FloatMap<ItemConvertible> instance, Object o, @Nullable Entity user, BlockState state, WorldAccess world, BlockPos pos, ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.COMPOSTABLE).map(CompostableItemComponent::levelIncreaseChance).orElse(0.0f);
    }

    @Redirect(
        method = "emptyFullComposter",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForBoneMealUseRegistryEntry(ItemConvertible item, @Local World world) {
        return world.itematic$createStack(ItemKeys.BONE_MEAL);
    }

    @Redirect(
        method = "getInventory",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForBoneMealUseRegistryEntry(ItemConvertible item, @Local WorldAccess world) {
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
            return stack.itematic$hasComponent(ItemComponentTypes.COMPOSTABLE);
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
