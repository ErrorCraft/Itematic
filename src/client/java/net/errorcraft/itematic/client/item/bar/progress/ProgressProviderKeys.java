package net.errorcraft.itematic.client.item.bar.progress;

import net.minecraft.util.Identifier;

public class ProgressProviderKeys {
    public static final Identifier DAMAGE = of("damage");
    public static final Identifier ITEM_HOLDER_OCCUPANCY = of("item_holder_occupancy");

    private ProgressProviderKeys() {}

    private static Identifier of(String id) {
        return Identifier.ofVanilla(id);
    }
}
