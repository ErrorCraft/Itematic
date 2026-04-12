package net.errorcraft.itematic.item.smithing.template;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public interface SmithingTemplate {
    Codec<SmithingTemplate> CODEC = ItematicRegistries.SMITHING_TEMPLATE.getCodec();

    List<Identifier> emptyBaseSlotTextures();
    List<Identifier> emptyAdditionsSlotTextures();
    Text baseSlotDescription();
    Text additionsSlotDescription();
}
