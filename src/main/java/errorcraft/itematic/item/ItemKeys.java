package errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ItemKeys {
    public static final RegistryKey<Item> AIR = register("air");
    public static final RegistryKey<Item> STONE = register("stone");

    private static RegistryKey<Item> register(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, new Identifier(name));
    }
}
