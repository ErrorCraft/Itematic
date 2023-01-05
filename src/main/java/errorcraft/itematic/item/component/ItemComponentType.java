package errorcraft.itematic.item.component;

import com.mojang.serialization.Codec;

public record ItemComponentType(Codec<? extends ItemComponent> codec) {
}
