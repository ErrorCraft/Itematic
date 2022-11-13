package errorcraft.itematic.inventory;

import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.registry.DynamicRegistryManager;

import java.util.function.BiConsumer;

public class SlotUtil {
    private static final String SLOT = "Slot";

    public static void writeToNbt(DynamicRegistryManager registryManager, int slot, ItemStack itemStack, NbtList nbt) {
        if (itemStack.isEmpty()) {
            return;
        }
        nbt.add(writeToNbt(registryManager, slot, itemStack, new NbtCompound()));
    }

    public static NbtCompound writeToNbt(DynamicRegistryManager registryManager, int slot, ItemStack itemStack, NbtCompound nbt) {
        nbt.putByte(SLOT, ((byte) slot));
        ItemStackUtil.writeToNbt(registryManager, itemStack, nbt);
        return nbt;
    }

    public static void readFromNbt(DynamicRegistryManager registryManager, Inventory inventory, NbtCompound nbt) {
        readFromNbt(registryManager, inventory::setStack, nbt);
    }

    public static void readFromNbt(DynamicRegistryManager registryManager, BiConsumer<Integer, ItemStack> slotSetter, NbtCompound nbt) {
        int slot = nbt.getByte(SLOT) & 0xFF;
        ItemStack itemStack = ItemStackUtil.readFromNbt(registryManager, nbt);
        if (itemStack.isEmpty()) {
            return;
        }
        slotSetter.accept(slot, itemStack);
    }
}
