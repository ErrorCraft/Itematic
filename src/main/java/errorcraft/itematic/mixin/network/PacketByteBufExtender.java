package errorcraft.itematic.mixin.network;

import errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.collection.IndexedIterable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PacketByteBuf.class)
public class PacketByteBufExtender implements DynamicRegistryManagerAccess {
    private DynamicRegistryManager registryManager;

    @ModifyArg(
        method = "readItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;readRegistryValue(Lnet/minecraft/util/collection/IndexedIterable;)Ljava/lang/Object;"
        )
    )
    private IndexedIterable<Item> readItemStackUseDynamicRegistry(IndexedIterable<Item> registry) {
        return this.registryManager.get(RegistryKeys.ITEM);
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
}
