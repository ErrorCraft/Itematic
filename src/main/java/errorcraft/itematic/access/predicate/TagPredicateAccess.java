package errorcraft.itematic.access.predicate;

import net.minecraft.registry.tag.TagKey;

public interface TagPredicateAccess<T> {
    default TagKey<T> getTag() {
        return null;
    }
    default boolean isExpected() {
        return false;
    }
}
