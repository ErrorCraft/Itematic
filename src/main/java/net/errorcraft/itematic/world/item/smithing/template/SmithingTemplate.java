package net.errorcraft.itematic.world.item.smithing.template;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.List;

public interface SmithingTemplate {
    Codec<SmithingTemplate> CODEC = ItematicBuiltInRegistries.SMITHING_TEMPLATE.byNameCodec();

    List<Identifier> emptyBaseSlotTextures();
    List<Identifier> emptyAdditionsSlotTextures();
    Component baseSlotDescription();
    Component additionsSlotDescription();
}
