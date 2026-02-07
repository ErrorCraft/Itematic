package net.errorcraft.itematic.client.item.bar.color;

import net.minecraft.util.Identifier;

public class ColorProviderTypeKeys {
    public static final Identifier CONSTANT = of("constant");
    public static final Identifier HUE_SHIFT = of("hue_shift");
    public static final Identifier FIRST_TO_PASS_CONDITION = of("first_to_pass_condition");

    private ColorProviderTypeKeys() {}

    private static Identifier of(String id) {
        return Identifier.ofVanilla(id);
    }
}
