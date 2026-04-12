package net.errorcraft.itematic.item;

import net.errorcraft.itematic.mixin.item.SmithingTemplateItemAccessor;
import net.errorcraft.itematic.util.Util;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Tooltips {
    private Tooltips() {}

    public static Text description(RegistryKey<Item> item) {
        return Text.translatable(Util.descriptionKey("item", item.getValue(), "desc"))
            .formatted(Formatting.GRAY);
    }

    public static Text[] smithingUpgrade(Identifier upgradeName) {
        return smithing(
            Text.translatable(Util.descriptionKey("smithing_template", upgradeName, "applies_to")),
            Text.translatable(Util.descriptionKey("smithing_template", upgradeName, "ingredients"))
        );
    }

    public static Text[] smithingTrimPattern() {
        return smithing(
            SmithingTemplateItemAccessor.trimPatternAppliesToLabel(),
            SmithingTemplateItemAccessor.trimPatternIngredients()
        );
    }

    private static Text[] smithing(Text appliesTo, Text ingredients) {
        return new Text[] {
            SmithingTemplateItemAccessor.smithingTemplateTitle(),
            ScreenTexts.EMPTY,
            SmithingTemplateItemAccessor.appliesToTitle(),
            ScreenTexts.space().append(appliesTo),
            SmithingTemplateItemAccessor.ingredientsTitle(),
            ScreenTexts.space().append(ingredients)
        };
    }
}
