package net.errorcraft.itematic.inventory;

import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.BiConsumer;

public class SlotUtil {
    private static final String SLOT_KEY = "Slot";

    public static void readFromNbt(NbtCompound nbt, DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks) {
        readFromNbt(nbt, registryManager, itemStacks::set);
    }

    public static void readFromNbt(NbtCompound nbt, DynamicRegistryManager registryManager, BiConsumer<Integer, ItemStack> slotSetter) {
        int slot = nbt.getByte(SLOT_KEY) & 0xFF;
        ItemStack itemStack = ItemStackUtil.readFromNbt(nbt, registryManager);
        if (itemStack.isEmpty()) {
            return;
        }
        slotSetter.accept(slot, itemStack);
    }
}
