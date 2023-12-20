package net.errorcraft.itematic.item.smithing.template;

import com.mojang.serialization.Codec;

public record SmithingTemplateType<T extends SmithingTemplate>(Codec<T> codec) {
}
