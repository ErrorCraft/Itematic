package net.errorcraft.itematic.item.smithing.template.templates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateType;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateTypes;
import net.errorcraft.itematic.mixin.item.SmithingTemplateItemAccessor;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public record ItemUpgradeSmithingTemplate(RegistryEntry<Item> item, Identifier translationKeyId) implements SmithingTemplate {
    public static final MapCodec<ItemUpgradeSmithingTemplate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(ItemUpgradeSmithingTemplate::item),
        Identifier.CODEC.fieldOf("translation_key_id").forGetter(ItemUpgradeSmithingTemplate::translationKeyId)
    ).apply(instance, ItemUpgradeSmithingTemplate::new));
    private static final List<Identifier> EMPTY_BASE_SLOT_TEXTURES = SmithingTemplateItemAccessor.getItemUpgradeEmptyBaseSlotTextures();
    private static final List<Identifier> EMPTY_ADDITIONS_SLOT_TEXTURES = SmithingTemplateItemAccessor.getItemUpgradeEmptyAdditionsSlotTextures();
    private static final Text BASE_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.getItemUpgradeBaseSlotDescriptionText();
    private static final Text ADDITIONS_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.getItemUpgradeAdditionsSlotDescriptionText();

    @Override
    public SmithingTemplateType<?> type() {
        return SmithingTemplateTypes.ITEM_UPGRADE;
    }

    @Override
    public MutableText titleText() {
        return Text.translatable(Util.createTranslationKey("upgrade", this.translationKeyId));
    }

    @Override
    public MutableText appliesToText() {
        return Text.translatable(this.createTranslationKey("applies_to"));
    }

    @Override
    public MutableText ingredientsText() {
        return Text.translatable(this.createTranslationKey("ingredients"));
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

    private String createTranslationKey(String type) {
        return Util.createTranslationKey("item", this.translationKeyId.withPath(id -> "smithing_template." + id + "." + type));
    }
}
