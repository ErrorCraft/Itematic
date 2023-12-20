package net.errorcraft.itematic.item.smithing.template;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class SmithingTemplateTypeKeys {
    public static final RegistryKey<SmithingTemplateType<?>> TRIM_PATTERN = of("trim_pattern");
    public static final RegistryKey<SmithingTemplateType<?>> ITEM_UPGRADE = of("item_upgrade");

    private SmithingTemplateTypeKeys() {}

    private static RegistryKey<SmithingTemplateType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.SMITHING_TEMPLATE_TYPE, new Identifier(id));
    }
}
