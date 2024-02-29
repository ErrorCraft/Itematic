package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Comparator;

@Mixin(ItemGroups.class)
public interface ItemGroupsAccessor {
    @Accessor("PAINTING_VARIANT_COMPARATOR")
    static Comparator<RegistryEntry<PaintingVariant>> paintingVariantComparator() {
        throw new AssertionError();
    }

    @Accessor("displayContext")
    static ItemGroup.DisplayContext displayContext() {
        throw new AssertionError();
    }

    @Accessor("displayContext")
    static void setDisplayContext(ItemGroup.DisplayContext displayContext) {}
}
