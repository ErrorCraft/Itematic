package errorcraft.itematic.inventory;

import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.BiConsumer;

public class SlotUtil {
    private static final String SLOT_KEY = "Slot";

    public static void writeToNbt(NbtList nbt, int slot, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }
        nbt.add(writeToNbt(new NbtCompound(), slot, itemStack));
    }

    public static NbtCompound writeToNbt(NbtCompound nbt, int slot, ItemStack itemStack) {
        nbt.putByte(SLOT_KEY, ((byte) slot));
        itemStack.writeNbt(nbt);
        return nbt;
    }

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
