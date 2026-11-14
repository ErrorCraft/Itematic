package net.errorcraft.itematic.references;

import net.minecraft.resources.Identifier;

public class SmithingTemplateDisplayIds {
    public static final Identifier TRIM_PATTERN = createKey("trim_pattern");
    public static final Identifier NETHERITE_UPGRADE = createKey("netherite_upgrade");

    private SmithingTemplateDisplayIds() {}

    private static Identifier createKey(String name) {
        return Identifier.withDefaultNamespace(name);
    }
}
