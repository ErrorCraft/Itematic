package net.errorcraft.itematic.mixin.network;

import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PacketByteBuf.class)
public abstract class PacketByteBufExtender extends ByteBuf implements DynamicRegistryManagerAccess {
    @Shadow
    public abstract int readVarInt();

    @Shadow
    @Nullable
    public abstract NbtCompound readNbt();

    private DynamicRegistryManager registryManager;

    @Inject(
        method = "readItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;readRegistryValue(Lnet/minecraft/util/collection/IndexedIterable;)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void readItemStackUseRegistryEntry(CallbackInfoReturnable<ItemStack> info) {
        Registry<Item> registry = this.registryManager.get(RegistryKeys.ITEM);
        RegistryEntry<Item> entry = this.readRegistryEntry(registry);
        byte count = this.readByte();
        ItemStack itemStack = ItemStackUtil.ofNullable(entry, count);
        itemStack.setNbt(this.readNbt());
        info.setReturnValue(itemStack);
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
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    @Nullable
    private <T> RegistryEntry<T> readRegistryEntry(Registry<T> registry) {
        int id = readVarInt();
        return registry.getEntry(id).orElse(null);
    }
}
