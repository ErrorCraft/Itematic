package net.errorcraft.itematic.mixin.server.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
import org.spongepowered.asm.mixin.injection.Redirect;

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
    private boolean alsoCheckWritableItemBehavior(boolean original, @Local ItemStack stack) {
        return original && stack.itematic$hasBehavior(ItemBehaviorType.WRITABLE);
    }

    @ModifyExpressionValue(
        method = "signBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private boolean alsoUseItemBehavior(boolean original, @Local ItemStack stack, @Share("writable") LocalRef<WritableItemBehavior> writable) {
        Optional<WritableItemBehavior> optionalWritable = stack.itematic$getBehavior(ItemBehaviorType.WRITABLE);
        optionalWritable.ifPresent(writable::set);
        return original && optionalWritable.isPresent();
    }

    @Redirect(
        method = "signBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForWrittenBookUseHolder(ItemStack instance, ItemLike item, @Share("writable") LocalRef<WritableItemBehavior> writable) {
        return instance.itematic$transmuteCopy(writable.get().transformsInto());
    }

    @Redirect(
        method = "handlePlaceRecipe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private PlacementInfo placementInfoUseDynamicRegistry(Recipe<?> instance) {
        return instance.itematic$placementInfo(this.player.registryAccess().lookupOrThrow(Registries.ITEM));
    }
}
