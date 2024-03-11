package net.errorcraft.itematic.mixin.component.type;

import net.minecraft.component.type.BundleContentsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BundleContentsComponent.class)
public interface BundleContentsComponentAccessor {
    @Accessor("NESTED_BUNDLE_OCCUPANCY")
    static int nestedItemHolderOccupancy() {
        throw new AssertionError();
    }
}
