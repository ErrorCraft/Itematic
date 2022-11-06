package errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;

public class ItemStackUtil {
    private static final Identifier AIR = ItemKeys.AIR.getValue();

    public static NbtCompound writeToNbt(DynamicRegistryManager registryManager, ItemStack itemStack, NbtCompound nbt) {
        Registry<Item> registry = registryManager.get(Registry.ITEM_KEY);
        Identifier id = registry.getId(itemStack.getItem());
        itemStack.writeNbt(nbt);
        if (id == null) {
            id = AIR;
        }
        nbt.putString("id", id.toString());
        return nbt;
    }

    public static ItemStack readFromNbt(DynamicRegistryManager registryManager, NbtCompound nbt) {
        ItemStack itemStack = ItemStack.fromNbt(nbt);
        Registry<Item> registry = registryManager.get(Registry.ITEM_KEY);
        Item item = registry.get(new Identifier(nbt.getString("id")));
        ItemStack actualItemStack = new ItemStack(item, itemStack.getCount());
        actualItemStack.setNbt(itemStack.getNbt());
        return actualItemStack;
    }
}
