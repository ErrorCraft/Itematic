package errorcraft.itematic.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.DynamicRegistryManager;

import java.util.function.BiConsumer;

public class InventoryUtil {
    public static void writeToNbt(DynamicRegistryManager registryManager, DefaultedList<ItemStack> itemStacks, NbtList nbt) {
        for (int i = 0; i < itemStacks.size(); i++) {
            SlotUtil.writeToNbt(registryManager, i, itemStacks.get(i), nbt);
        }
    }

    public static Inventory readFromNbt(DynamicRegistryManager registryManager, Inventory inventory, NbtList nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            SlotUtil.readFromNbt(registryManager, inventory, nbt.getCompound(i));
        }
        return inventory;
    }

    public static void readFromNbt(DynamicRegistryManager registryManager, BiConsumer<Integer, ItemStack> slotSetter, NbtList nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            SlotUtil.readFromNbt(registryManager, slotSetter, nbt.getCompound(i));
        }
    }
}
