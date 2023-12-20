package net.errorcraft.itematic.item.smithing.template;

import net.errorcraft.itematic.item.smithing.template.templates.ItemUpgradeSmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.templates.TrimPatternSmithingTemplate;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class SmithingTemplateTypes {
    public static final SmithingTemplateType<?> TRIM_PATTERN = register(SmithingTemplateTypeKeys.TRIM_PATTERN, new SmithingTemplateType<>(TrimPatternSmithingTemplate.CODEC));
    public static final SmithingTemplateType<?> ITEM_UPGRADE = register(SmithingTemplateTypeKeys.ITEM_UPGRADE, new SmithingTemplateType<>(ItemUpgradeSmithingTemplate.CODEC));

    private SmithingTemplateTypes() {}

    public static void init() {}

    private static <T extends SmithingTemplate> SmithingTemplateType<T> register(RegistryKey<SmithingTemplateType<?>> id, SmithingTemplateType<T> type) {
        return Registry.register(ItematicRegistries.SMITHING_TEMPLATE_TYPE, id, type);
    }
}
