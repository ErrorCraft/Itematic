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
        return instance.itematic$hasComponent(ItemComponentTypes.WRITABLE);
    }

    @Redirect(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForWritableBookUseItemComponentCheck(ItemStack instance, Item item, @Share("writableItemComponent") LocalRef<WritableItemComponent> writableItemComponent) {
        Optional<WritableItemComponent> optionalComponent = instance.itematic$getComponent(ItemComponentTypes.WRITABLE);
        optionalComponent.ifPresent(writableItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;copyNbtToNewStack(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack copyWithItemForWrittenBookUseItemComponent(ItemStack instance, ItemConvertible itemConvertible, int count, @Share("writableItemComponent") LocalRef<WritableItemComponent> writableItemComponent) {
        return instance.itematic$copyWithItem(writableItemComponent.get().transformsInto(), count);
    }
}
