package errorcraft.itematic.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.BiConsumer;

public class InventoryUtil {
    private static final String ITEMS = "Items";

    public static NbtCompound writeToNbt(NbtCompound nbt, DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks) {
        return writeToNbt(nbt, registryManager, itemStacks, true);
    }

    public static NbtCompound writeToNbt(NbtCompound nbt, DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks, boolean setIfEmpty) {
        NbtList list = writeToNbt(new NbtList(), registryManager, itemStacks);
        if (!list.isEmpty() || setIfEmpty) {
            nbt.put(ITEMS, list);
        }
        return nbt;
    }

    public static NbtList writeToNbt(NbtList nbt, DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks) {
        for (int i = 0; i < itemStacks.size(); i++) {
            SlotUtil.writeToNbt(nbt, registryManager, i, itemStacks.get(i));
        }
        return nbt;
    }

    public static void readFromNbt(NbtCompound nbt, DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks) {
        NbtList list = nbt.getList(ITEMS, NbtElement.COMPOUND_TYPE);
        readFromNbt(list, registryManager, itemStacks);
    }

    public static void readFromNbt(NbtList nbt, DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks) {
        for (int i = 0; i < nbt.size(); i++) {
            SlotUtil.readFromNbt(nbt.getCompound(i), registryManager, itemStacks);
        }
    }

    public static void readFromNbt(NbtList nbt, DynamicRegistryManager registryManager, BiConsumer<Integer, ItemStack> slotSetter) {
        for (int i = 0; i < nbt.size(); i++) {
            SlotUtil.readFromNbt(nbt.getCompound(i), registryManager, slotSetter);
        }
    }
}
