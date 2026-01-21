package net.errorcraft.itematic.item;

import net.minecraft.util.Identifier;

public class ItemBarStyleKeys {
    public static final Identifier DAMAGE = of("damage");
    public static final Identifier BUNDLE = of("bundle");

    private ItemBarStyleKeys() {}

    private static Identifier of(String name) {
        return Identifier.ofVanilla(name);
    }
}
