package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.mixin.item.SmithingTemplateItemAccessor;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public record SmithingTemplateItemComponent(RegistryEntry<SmithingTemplate> template) implements ItemComponent<SmithingTemplateItemComponent> {
    public static final Codec<SmithingTemplateItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(ItematicRegistryKeys.SMITHING_TEMPLATE).fieldOf("template").forGetter(SmithingTemplateItemComponent::template)
    ).apply(instance, SmithingTemplateItemComponent::new));
    private static final Formatting DESCRIPTION_FORMATTING = SmithingTemplateItemAccessor.getDescriptionFormatting();
    private static final Text SMITHING_TEMPLATE_TEXT = SmithingTemplateItemAccessor.getSmithingTemplateText();
    private static final Text APPLIES_TO_TEXT = SmithingTemplateItemAccessor.getAppliesToText();
    private static final Text INGREDIENTS_TEXT = SmithingTemplateItemAccessor.getIngredientsText();

    public static ItemComponent<?>[] of(RegistryEntry<SmithingTemplate> template) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(64),
            new SmithingTemplateItemComponent(template)
        };
    }

    @Override
    public ItemComponentType<SmithingTemplateItemComponent> type() {
        return ItemComponentTypes.SMITHING_TEMPLATE;
    }

    @Override
    public Codec<SmithingTemplateItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        SmithingTemplate template = this.template.value();
        tooltip.add(SMITHING_TEMPLATE_TEXT);
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(APPLIES_TO_TEXT);
        tooltip.add(ScreenTexts.space().append(template.appliesToText().formatted(DESCRIPTION_FORMATTING)));
        tooltip.add(INGREDIENTS_TEXT);
        tooltip.add(ScreenTexts.space().append(template.ingredientsText().formatted(DESCRIPTION_FORMATTING)));
    }
}
