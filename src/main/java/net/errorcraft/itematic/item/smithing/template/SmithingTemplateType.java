package net.errorcraft.itematic.item.smithing.template;

import com.mojang.serialization.MapCodec;

public record SmithingTemplateType<T extends SmithingTemplate>(MapCodec<T> codec) {
}
