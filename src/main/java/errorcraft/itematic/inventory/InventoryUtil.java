package errorcraft.itematic.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.BiConsumer;

public class InventoryUtil {
    private static final String ITEMS_KEY = "Items";

    public static void readFromNbt(NbtCompound nbt, DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks) {
        NbtList list = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
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
