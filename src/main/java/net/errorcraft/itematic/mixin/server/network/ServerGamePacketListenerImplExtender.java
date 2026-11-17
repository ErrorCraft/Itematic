package net.errorcraft.itematic.mixin.server.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplExtender {
    @ModifyExpressionValue(
        method = "signBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private boolean storeSignResultItem(boolean original, @Local(name = "carried") ItemStack carried, @Share("signResultItem") LocalRef<@Nullable Holder<Item>> signResultItemReference) {
        if (!original) {
            return false;
        }

        Holder<Item> signResultItem = carried.get(ItematicDataComponents.SIGN_RESULT_ITEM);
        if (signResultItem != null) {
            signResultItemReference.set(signResultItem);
        }

        return true;
    }

    @WrapOperation(
        method = "signBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForWrittenBookUseDataComponent(ItemStack instance, ItemLike newItem, Operation<ItemStack> original, @Share("signResultItem") LocalRef<@Nullable Holder<Item>> signResultItemReference) {
        Holder<Item> signResultItem = signResultItemReference.get();
        if (signResultItem == null) {
            return instance.copy();
        }

        return instance.itematic$transmuteCopy(signResultItem);
    }

    @WrapOperation(
        method = "handleSetCreativeModeSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"
        )
    )
    private boolean doNotCheckEnabledItem(ItemStack instance, FeatureFlagSet enabledFeatures, Operation<Boolean> original) {
        return true;
    }
}
