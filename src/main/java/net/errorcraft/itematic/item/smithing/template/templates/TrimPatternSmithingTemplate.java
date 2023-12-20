package net.errorcraft.itematic.item.smithing.template.templates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateType;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateTypes;
import net.errorcraft.itematic.mixin.item.SmithingTemplateItemAccessor;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public record TrimPatternSmithingTemplate(RegistryEntry<ArmorTrimPattern> trimPattern) implements SmithingTemplate {
    public static final Codec<TrimPatternSmithingTemplate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.TRIM_PATTERN).fieldOf("trim_pattern").forGetter(TrimPatternSmithingTemplate::trimPattern)
    ).apply(instance, TrimPatternSmithingTemplate::new));
    private static final MutableText APPLIES_TO_TEXT = (MutableText) SmithingTemplateItemAccessor.getTrimPatternAppliesToText();
    private static final MutableText INGREDIENTS_TEXT = (MutableText) SmithingTemplateItemAccessor.getTrimPatternIngredientsText();
    private static final List<Identifier> EMPTY_BASE_SLOT_TEXTURES = SmithingTemplateItemAccessor.getTrimPatternEmptyBaseSlotTextures();
    private static final List<Identifier> EMPTY_ADDITIONS_SLOT_TEXTURES = SmithingTemplateItemAccessor.getTrimPatternEmptyAdditionsSlotTextures();
    private static final Text BASE_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.getTrimPatternBaseSlotDescriptionText();
    private static final Text ADDITIONS_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.getTrimPatternAdditionsSlotDescriptionText();

    @Override
    public SmithingTemplateType<?> type() {
        return SmithingTemplateTypes.TRIM_PATTERN;
    }

    @Override
    public MutableText titleText() {
        Identifier id = this.trimPattern.getKey().map(RegistryKey::getValue).orElse(null);
        return Text.translatable(Util.createTranslationKey("trim_pattern", id));
    }

    @Override
    public MutableText appliesToText() {
        return APPLIES_TO_TEXT;
    }

    @Override
    public MutableText ingredientsText() {
        return INGREDIENTS_TEXT;
    }

    @Override
    public List<Identifier> emptyBaseSlotTextures() {
        return EMPTY_BASE_SLOT_TEXTURES;
    }

    @Override
    public List<Identifier> emptyAdditionsSlotTextures() {
        return EMPTY_ADDITIONS_SLOT_TEXTURES;
    }

    @Override
    public Text baseSlotDescription() {
        return BASE_SLOT_DESCRIPTION;
    }

    @Override
    public Text additionsSlotDescription() {
        return ADDITIONS_SLOT_DESCRIPTION;
    }
}
