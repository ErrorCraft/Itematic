package errorcraft.itematic.mixin.predicate;

import errorcraft.itematic.access.predicate.TagPredicateAccess;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TagPredicate.class)
public class TagPredicateExtender<T> implements TagPredicateAccess<T> {
    @Shadow
    @Final
    private TagKey<T> tag;

    @Shadow
    @Final
    private boolean expected;

    @Override
    public TagKey<T> getTag() {
        return this.tag;
    }

    @Override
    public boolean isExpected() {
        return this.expected;
    }
}
