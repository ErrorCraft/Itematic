package net.errorcraft.itematic.advancements.predicates;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.mixin.advancements.predicates.MinMaxBoundsAccessor;
import net.minecraft.advancements.predicates.MinMaxBounds;

public class ItematicMinMaxBounds {
    private ItematicMinMaxBounds() {}

    public record Floats(Bounds<Float> bounds) implements MinMaxBounds<Float> {
        public static final Codec<Floats> CODEC = MinMaxBoundsAccessor.BoundsAccessor.createCodec(Codec.FLOAT)
            .xmap(Floats::new, Floats::bounds);

        public static Floats exactly(float value) {
            return new Floats(Bounds.exactly(value));
        }

        public boolean test(float value) {
            if (this.bounds.min().isPresent() && this.bounds.min().get() > value) {
                return false;
            }

            return this.bounds.max().isEmpty() || value <= this.bounds.max().get();
        }
    }
}
