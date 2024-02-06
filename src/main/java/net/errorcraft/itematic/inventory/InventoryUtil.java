package net.errorcraft.itematic.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.BiConsumer;

public class InventoryUtil {
    private static final String ITEMS_KEY = "Items";

    public static void readFromNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtList items = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        readFromNbt(items, stacks, wrapperLookup);
    }

    public static void readFromNbt(NbtList items, DefaultedList<ItemStack> stacks, RegistryWrapper.WrapperLookup wrapperLookup) {
        for (int i = 0; i < items.size(); i++) {
            SlotUtil.readFromNbt(items.getCompound(i), stacks, wrapperLookup);
        }
    }

    public static void readFromNbt(NbtList nbt, RegistryWrapper.WrapperLookup wrapperLookup, BiConsumer<Integer, ItemStack> slotSetter) {
        for (int i = 0; i < nbt.size(); i++) {
            SlotUtil.readFromNbt(nbt.getCompound(i), wrapperLookup, slotSetter);
        }
    }
}
