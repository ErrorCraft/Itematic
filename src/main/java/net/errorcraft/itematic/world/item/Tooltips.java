package net.errorcraft.itematic.world.item;

import net.errorcraft.itematic.mixin.world.item.SmithingTemplateItemAccessor;
import net.errorcraft.itematic.util.ItematicUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class Tooltips {
    private Tooltips() {}

    public static Component description(ResourceKey<Item> item) {
        return Component.translatable(ItematicUtil.descriptionKey("item", item.identifier(), "desc"))
            .withStyle(ChatFormatting.GRAY);
    }

    public static Component[] netheriteUpgrade() {
        return smithing(
            SmithingTemplateItemAccessor.netheriteUpgradeAppliesToLabel(),
            SmithingTemplateItemAccessor.netheriteUpgradeIngredients()
        );
    }

    public static Component[] smithingTrimPattern() {
        return smithing(
            SmithingTemplateItemAccessor.trimPatternAppliesToLabel(),
            SmithingTemplateItemAccessor.trimPatternIngredients()
        );
    }

    private static Component[] smithing(Component appliesTo, Component ingredients) {
        return new Component[] {
            SmithingTemplateItemAccessor.smithingTemplateTitle(),
            CommonComponents.EMPTY,
            SmithingTemplateItemAccessor.appliesToTitle(),
            CommonComponents.space().append(appliesTo),
            SmithingTemplateItemAccessor.ingredientsTitle(),
            CommonComponents.space().append(ingredients)
        };
    }
}
