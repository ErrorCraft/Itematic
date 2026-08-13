package net.errorcraft.itematic.references;

import net.minecraft.resources.Identifier;

public class ItemBarStyleIds {
    public static final Identifier DAMAGE = of("damage");
    public static final Identifier BUNDLE = of("bundle");

    private ItemBarStyleIds() {}

    private static Identifier of(String name) {
        return Identifier.withDefaultNamespace(name);
    }
}
