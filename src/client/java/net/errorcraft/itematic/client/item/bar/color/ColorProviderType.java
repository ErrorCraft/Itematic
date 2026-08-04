package net.errorcraft.itematic.client.item.bar.color;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

public record ColorProviderType<T extends ColorProvider>(Identifier id, MapCodec<T> codec) {
}
