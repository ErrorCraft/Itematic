package net.errorcraft.itematic.mixin.network;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.access.network.PacketByteBufAccess;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.IndexedIterable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PacketByteBuf.class)
public abstract class PacketByteBufExtender extends ByteBuf implements PacketByteBufAccess {
    @Shadow
    public abstract int readVarInt();

    private DynamicRegistryManager registryManager;

    @Redirect(
        method = "readItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;readRegistryValue(Lnet/minecraft/util/collection/IndexedIterable;)Ljava/lang/Object;"
        )
    )
    private <T> T returnNullForItemRegistryEntry(PacketByteBuf instance, IndexedIterable<T> registry) {
        return null;
    }

    @Inject(
        method = "readItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;readByte()B"
        )
    )
    private void storeItemRegistryEntry(CallbackInfoReturnable<ItemStack> info, @Share("entry") LocalRef<RegistryEntry<Item>> entry) {
        Registry<Item> registry = this.registryManager.get(RegistryKeys.ITEM);
        entry.set(this.readRegistryEntry(registry));
    }

    @Redirect(
        method = "readItemStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack readItemStackNewItemStackUseRegistryEntry(ItemConvertible item, int count, @Share("entry") LocalRef<RegistryEntry<Item>> entry) {
        return ItemStackUtil.ofNullable(entry.get(), count);
    }

    @Redirect(
        method = "writeItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item getItemUseRegistryEntry(ItemStack instance) {
        return instance.getRegistryEntry().value();
    }

    @ModifyArg(
        method = "writeItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;writeRegistryValue(Lnet/minecraft/util/collection/IndexedIterable;Ljava/lang/Object;)V"
        )
    )
    private IndexedIterable<Item> writeItemStackUseDynamicRegistry(IndexedIterable<Item> registry) {
        return this.registryManager.get(RegistryKeys.ITEM);
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    @Nullable
    private <T> RegistryEntry<T> readRegistryEntry(Registry<T> registry) {
        int id = this.readVarInt();
        return registry.getEntry(id).orElse(null);
    }
}
