package net.errorcraft.itematic.item.smithing.template;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.smithing.template.templates.ItemUpgradeSmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.templates.TrimPatternSmithingTemplate;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.item.trim.ArmorTrimPatterns;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class SmithingTemplates {
    public static final RegistryKey<SmithingTemplate> NETHERITE_UPGRADE = of("netherite_upgrade");
    public static final RegistryKey<SmithingTemplate> COAST_PATTERN = of("coast_pattern");
    public static final RegistryKey<SmithingTemplate> DUNE_PATTERN = of("dune_pattern");
    public static final RegistryKey<SmithingTemplate> EYE_PATTERN = of("eye_pattern");
    public static final RegistryKey<SmithingTemplate> HOST_PATTERN = of("host_pattern");
    public static final RegistryKey<SmithingTemplate> RAISER_PATTERN = of("raiser_pattern");
    public static final RegistryKey<SmithingTemplate> RIB_PATTERN = of("rib_pattern");
    public static final RegistryKey<SmithingTemplate> SENTRY_PATTERN = of("sentry_pattern");
    public static final RegistryKey<SmithingTemplate> SHAPER_PATTERN = of("shaper_pattern");
    public static final RegistryKey<SmithingTemplate> SILENCE_PATTERN = of("silence_pattern");
    public static final RegistryKey<SmithingTemplate> SNOUT_PATTERN = of("snout_pattern");
    public static final RegistryKey<SmithingTemplate> SPIRE_PATTERN = of("spire_pattern");
    public static final RegistryKey<SmithingTemplate> TIDE_PATTERN = of("tide_pattern");
    public static final RegistryKey<SmithingTemplate> VEX_PATTERN = of("vex_pattern");
    public static final RegistryKey<SmithingTemplate> WARD_PATTERN = of("ward_pattern");
    public static final RegistryKey<SmithingTemplate> WAYFINDER_PATTERN = of("wayfinder_pattern");
    public static final RegistryKey<SmithingTemplate> WILD_PATTERN = of("wild_pattern");

    private SmithingTemplates() {}

    public static void bootstrap(Registerable<SmithingTemplate> registerable) {
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<ArmorTrimPattern> trimPatterns = registerable.getRegistryLookup(RegistryKeys.TRIM_PATTERN);

        registerable.register(NETHERITE_UPGRADE, new ItemUpgradeSmithingTemplate(items.getOrThrow(ItemKeys.NETHERITE_INGOT), new Identifier("netherite_upgrade")));
        registerable.register(COAST_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.COAST)));
        registerable.register(DUNE_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.DUNE)));
        registerable.register(EYE_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.EYE)));
        registerable.register(HOST_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.HOST)));
        registerable.register(RAISER_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.RAISER)));
        registerable.register(RIB_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.RIB)));
        registerable.register(SENTRY_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.SENTRY)));
        registerable.register(SHAPER_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.SHAPER)));
        registerable.register(SILENCE_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.SILENCE)));
        registerable.register(SNOUT_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.SNOUT)));
        registerable.register(SPIRE_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.SPIRE)));
        registerable.register(TIDE_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.TIDE)));
        registerable.register(VEX_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.VEX)));
        registerable.register(WARD_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.WARD)));
        registerable.register(WAYFINDER_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.WAYFINDER)));
        registerable.register(WILD_PATTERN, new TrimPatternSmithingTemplate(trimPatterns.getOrThrow(ArmorTrimPatterns.WILD)));
    }

    private static RegistryKey<SmithingTemplate> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.SMITHING_TEMPLATE, new Identifier(id));
    }
}
