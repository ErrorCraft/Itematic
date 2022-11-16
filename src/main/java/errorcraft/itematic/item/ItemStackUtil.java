package errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ItemStackUtil {
    private static final String ID = "id";
    private static final Identifier AIR = ItemKeys.AIR.getValue();

    public static NbtCompound writeToNbt(NbtCompound nbt, DynamicRegistryManager registryManager, ItemStack itemStack) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        Identifier id = registry.getId(itemStack.getItem());
        itemStack.writeNbt(nbt);
        if (id == null) {
            id = AIR;
        }
        nbt.putString(ID, id.toString());
        return nbt;
    }

    public static ItemStack readFromNbt(NbtCompound nbt, DynamicRegistryManager registryManager) {
        ItemStack itemStack = ItemStack.fromNbt(nbt);
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        Item item = registry.get(new Identifier(nbt.getString(ID)));
        ItemStack actualItemStack = new ItemStack(item, itemStack.getCount());
        actualItemStack.setNbt(itemStack.getNbt());
        return actualItemStack;
    }
}
