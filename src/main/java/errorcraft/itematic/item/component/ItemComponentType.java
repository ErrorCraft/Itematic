package errorcraft.itematic.item.component;

import com.mojang.serialization.Codec;

public record ItemComponentType<T extends ItemComponent>(Codec<T> codec) {
}
