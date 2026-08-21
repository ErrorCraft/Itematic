package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Comparator;

@Mixin(CreativeModeTabs.class)
public interface CreativeModeTabsAccessor {
    @Accessor("PAINTING_COMPARATOR")
    static Comparator<Holder<PaintingVariant>> paintingVariantComparator() {
        throw new AssertionError();
    }

    @Accessor("CACHED_PARAMETERS")
    static void setCachedParameters(CreativeModeTab.@Nullable ItemDisplayParameters parameters) {}
}
