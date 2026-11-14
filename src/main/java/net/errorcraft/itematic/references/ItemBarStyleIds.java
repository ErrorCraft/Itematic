package net.errorcraft.itematic.references;

import net.minecraft.resources.Identifier;

public class ItemBarStyleIds {
    public static final Identifier DAMAGE = createKey("damage");
    public static final Identifier BUNDLE = createKey("bundle");

    private ItemBarStyleIds() {}

    private static Identifier createKey(String name) {
        return Identifier.withDefaultNamespace(name);
    }
}
