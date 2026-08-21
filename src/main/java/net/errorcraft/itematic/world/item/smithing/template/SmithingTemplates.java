package net.errorcraft.itematic.world.item.smithing.template;

import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.smithing.template.templates.ItemUpgradeSmithingTemplate;
import net.errorcraft.itematic.world.item.smithing.template.templates.TrimPatternSmithingTemplate;
import net.minecraft.core.Registry;

public class SmithingTemplates {
    public static final SmithingTemplate TRIM_PATTERN = register("trim_pattern", new TrimPatternSmithingTemplate());
    public static final SmithingTemplate ITEM_UPGRADE = register("item_upgrade", new ItemUpgradeSmithingTemplate());

    private SmithingTemplates() {}

    public static void init() {}

    private static SmithingTemplate register(String id, SmithingTemplate type) {
        return Registry.register(ItematicBuiltInRegistries.SMITHING_TEMPLATE, id, type);
    }
}
