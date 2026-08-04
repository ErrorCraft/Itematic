package net.errorcraft.itematic.mixin.component.type;

import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BundleContents.class)
public interface BundleContentsComponentAccessor {
    @Accessor("BUNDLE_IN_BUNDLE_WEIGHT")
    static Fraction nestedBundleOccupancy() {
        throw new AssertionError();
    }
}
