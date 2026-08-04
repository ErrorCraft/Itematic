package net.errorcraft.itematic.item.smithing.template;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.List;

public interface SmithingTemplate {
    Codec<SmithingTemplate> CODEC = ItematicRegistries.SMITHING_TEMPLATE.byNameCodec();

    List<Identifier> emptyBaseSlotTextures();
    List<Identifier> emptyAdditionsSlotTextures();
    Component baseSlotDescription();
    Component additionsSlotDescription();
}
