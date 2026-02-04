package net.errorcraft.itematic.mixin.server.network;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.WritableItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerExtender {
    @Redirect(
        method = "updateBookContent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForWritableBookUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemComponentTypes.WRITABLE);
    }

    @Redirect(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForWritableBookUseItemComponentCheck(ItemStack instance, Item item, @Share("writable") LocalRef<WritableItemComponent> writable) {
        Optional<WritableItemComponent> optionalWritable = instance.itematic$getBehavior(ItemComponentTypes.WRITABLE);
        optionalWritable.ifPresent(writable::set);
        return optionalWritable.isPresent();
    }

    @Redirect(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;withItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack withItemForWrittenBookUseItemComponent(ItemStack instance, ItemConvertible itemConvertible, @Share("writable") LocalRef<WritableItemComponent> writable) {
        return instance.itematic$copyWithItem(writable.get().transformsInto());
    }
}
