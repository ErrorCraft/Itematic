package net.errorcraft.itematic.item.smithing.template;

import net.errorcraft.itematic.item.smithing.template.templates.ItemUpgradeSmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.templates.TrimPatternSmithingTemplate;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;

public class SmithingTemplates {
    public static final SmithingTemplate TRIM_PATTERN = register("trim_pattern", new TrimPatternSmithingTemplate());
    public static final SmithingTemplate ITEM_UPGRADE = register("item_upgrade", new ItemUpgradeSmithingTemplate());

    private SmithingTemplates() {}

    public static void init() {}

    private static SmithingTemplate register(String id, SmithingTemplate type) {
        return Registry.register(ItematicRegistries.SMITHING_TEMPLATE, id, type);
    }
}
