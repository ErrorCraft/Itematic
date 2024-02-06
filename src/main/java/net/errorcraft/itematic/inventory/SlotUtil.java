package net.errorcraft.itematic.inventory;

import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.BiConsumer;

public class SlotUtil {
    private static final String SLOT_KEY = "Slot";

    public static void readFromNbt(NbtCompound nbt, DefaultedList<ItemStack> itemStacks, RegistryWrapper.WrapperLookup wrapperLookup) {
        readFromNbt(nbt, wrapperLookup, itemStacks::set);
    }

    public static void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup, BiConsumer<Integer, ItemStack> slotSetter) {
        int slot = nbt.getByte(SLOT_KEY) & 0xFF;
        ItemStack stack = ItemStackUtil.fromNbt(nbt, wrapperLookup);
        if (stack.isEmpty()) {
            return;
        }
        slotSetter.accept(slot, stack);
    }
}
