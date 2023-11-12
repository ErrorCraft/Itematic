package net.errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public class ItemStackUtil {
    public static ItemStack fromNbt(NbtCompound nbt, DynamicRegistryManager registryManager) {
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, registryManager);
        return ItemStack.CODEC.parse(ops, nbt).result().orElse(ItemStack.EMPTY);
    }

    public static int getRawId(@Nullable RegistryEntry<Item> entry) {
        if (entry == null) {
            return -1;
        }
        return entry.itematic$rawId();
    }
}
