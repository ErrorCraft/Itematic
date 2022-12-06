package errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ItemStackUtil {
    private static final String ID_KEY = "id";
    private static final String COUNT_KEY = "Count";
    private static final String NBT_KEY = "tag";
    private static final Identifier AIR = ItemKeys.AIR.getValue();

    public static NbtCompound writeToNbt(NbtCompound nbt, DynamicRegistryManager registryManager, ItemStack itemStack) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        Identifier id = registry.getId(itemStack.getItem());
        itemStack.writeNbt(nbt);
        if (id == null) {
            id = AIR;
        }
        nbt.putString(ID_KEY, id.toString());
        return nbt;
    }

    public static ItemStack readFromNbt(NbtCompound nbt, DynamicRegistryManager registryManager) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        Item item = registry.get(new Identifier(nbt.getString(ID_KEY)));
        int count = nbt.getByte(COUNT_KEY);
        ItemStack itemStack = new ItemStack(item, count);
        if (nbt.contains(NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            itemStack.setNbt(nbt.getCompound(NBT_KEY));
        }
        return itemStack;
    }
}
