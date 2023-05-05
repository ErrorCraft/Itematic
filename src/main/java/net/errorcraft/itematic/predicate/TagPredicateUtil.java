package net.errorcraft.itematic.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;

public class TagPredicateUtil {
    public static <T> Codec<TagPredicate<T>> codec(RegistryKey<? extends Registry<T>> registry) {
        return RecordCodecBuilder.create(instance -> instance.group(
            TagKey.unprefixedCodec(registry).fieldOf("id").forGetter(TagPredicate::getTag),
            Codec.BOOL.fieldOf("expected").forGetter(TagPredicate::isExpected)
        ).apply(instance, TagPredicate::new));
    }
}
