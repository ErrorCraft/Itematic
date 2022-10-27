package errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ItemKeys {
    public static final RegistryKey<Item> AIR = register("air");
    public static final RegistryKey<Item> STONE = register("stone");

    private static RegistryKey<Item> register(String name) {
        return RegistryKey.of(Registry.ITEM_KEY, new Identifier(name));
    }
}
