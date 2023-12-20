package net.errorcraft.itematic.item.smithing.template;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public interface SmithingTemplate {
    Codec<SmithingTemplate> CODEC = ItematicRegistries.SMITHING_TEMPLATE_TYPE.getCodec().dispatch(SmithingTemplate::type, SmithingTemplateType::codec);

    SmithingTemplateType<?> type();
    MutableText titleText();
    MutableText appliesToText();
    MutableText ingredientsText();
    List<Identifier> emptyBaseSlotTextures();
    List<Identifier> emptyAdditionsSlotTextures();
    Text baseSlotDescription();
    Text additionsSlotDescription();
}
