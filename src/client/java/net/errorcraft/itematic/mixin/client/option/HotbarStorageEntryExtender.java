package net.errorcraft.itematic.mixin.client.option;

import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.HotbarStorageEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HotbarStorageEntry.class)
public class HotbarStorageEntryExtender {
    @Redirect(
        method = "readNbtList",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack readNbtListUseDynamicRegistry(NbtCompound nbt) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world != null) {
            return ItemStackUtil.fromNbt(nbt, world.getRegistryManager());
        }
        return ItemStack.EMPTY;
    }
}
