package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerExtender {
    @Inject(
        method = "method_17410",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private void runStoreItemComponentVariable(ItemStack itemStack, int i, PlayerEntity playerEntity, int j, ItemStack itemStack2, World world, BlockPos pos, CallbackInfo info, @Share("enchantableItemComponent") LocalRef<Optional<EnchantableItemComponent>> enchantableItemComponent) {
        enchantableItemComponent.set(itemStack.itematic$getComponent(ItemComponentTypes.ENCHANTABLE));
    }

    @Redirect(
        method = "method_17410",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        remap = false
    )
    private boolean isOfForBookUseRegistryEntryCheck(ItemStack instance, Item item, @Share("enchantableItemComponent") LocalRef<Optional<EnchantableItemComponent>> enchantableItemComponent) {
        return enchantableItemComponent.get().flatMap(EnchantableItemComponent::transformsInto).isPresent();
    }

    @Redirect(
        method = "method_17410",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForEnchantedBookUseRegistryEntry(ItemConvertible item, ItemStack stack, int v, PlayerEntity player, int b, ItemStack stack2, World world, @Share("enchantableItemComponent") LocalRef<Optional<EnchantableItemComponent>> enchantableItemComponent) {
        return enchantableItemComponent.get()
            .flatMap(EnchantableItemComponent::transformsInto)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }

    @Redirect(
        method = "generateEnchantments",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBookUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOOK);
    }

    @Redirect(
        method = "quickMove",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForLapisLazuliUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.LAPIS_LAZULI);
    }

    @Mixin(targets = "net/minecraft/screen/EnchantmentScreenHandler$3")
    public static class LapisLazuliSlotExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean isOfForLapisLazuliUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.LAPIS_LAZULI);
        }
    }
}
