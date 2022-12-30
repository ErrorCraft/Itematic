package errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Util;

public record ItemBaseDisplay(String translationKey) {
    public static final Codec<ItemBaseDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("translation_key").forGetter(ItemBaseDisplay::translationKey)
    ).apply(instance, ItemBaseDisplay::new));

    public static class Builder {
        private final String translationKey;

        private Builder(String name) {
            this.translationKey = name;
        }

        public ItemBaseDisplay build() {
            return new ItemBaseDisplay(this.translationKey);
        }

        public static Builder forItem(RegistryKey<Item> name) {
            return new Builder(Util.createTranslationKey("item", name.getValue()));
        }

        public static Builder forBlock(RegistryKey<Item> name) {
            return new Builder(Util.createTranslationKey("block", name.getValue()));
        }
    }
}
