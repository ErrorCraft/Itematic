package net.errorcraft.itematic.client.resources.item.smithing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;

import java.util.List;

public record SmithingTemplateDisplay(Slot baseSlot, Slot additionsSlot) {
    public static final Codec<SmithingTemplateDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Slot.CODEC.fieldOf("base_slot").forGetter(SmithingTemplateDisplay::baseSlot),
        Slot.CODEC.fieldOf("additions_slot").forGetter(SmithingTemplateDisplay::additionsSlot)
    ).apply(instance, SmithingTemplateDisplay::new));

    public record Slot(Component description, List<Identifier> emptyTextures) {
        public static final Codec<Slot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("description").forGetter(Slot::description),
            Identifier.CODEC.listOf().fieldOf("empty_textures").forGetter(Slot::emptyTextures)
        ).apply(instance, Slot::new));
    }
}
