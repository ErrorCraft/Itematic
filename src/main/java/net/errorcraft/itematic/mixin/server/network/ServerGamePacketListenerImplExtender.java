package net.errorcraft.itematic.mixin.server.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.WritableItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplExtender {
    @Shadow
    public ServerPlayer player;

    @ModifyExpressionValue(
        method = "updateBookContents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private boolean alsoCheckWritableItemBehavior(boolean original, @Local(name = "carried") ItemStack carried) {
        return original && carried.itematic$hasBehavior(ItemBehaviorType.WRITABLE);
    }

    @ModifyExpressionValue(
        method = "signBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private boolean alsoUseItemBehavior(boolean original, @Local(name = "carried") ItemStack carried, @Share("writable") LocalRef<WritableItemBehavior> writableReference) {
        Optional<WritableItemBehavior> writable = carried.itematic$getBehavior(ItemBehaviorType.WRITABLE);
        writable.ifPresent(writableReference::set);
        return original && writable.isPresent();
    }

    @WrapOperation(
        method = "signBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForWrittenBookUseHolder(ItemStack instance, ItemLike newItem, Operation<ItemStack> original, @Share("writable") LocalRef<WritableItemBehavior> writableReference) {
        return instance.itematic$transmuteCopy(writableReference.get().transformsInto());
    }

    @WrapOperation(
        method = "handlePlaceRecipe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private PlacementInfo placementInfoUseDynamicRegistry(Recipe<?> instance, Operation<PlacementInfo> original) {
        return instance.itematic$placementInfo(this.player.registryAccess().lookupOrThrow(Registries.ITEM));
    }
}
