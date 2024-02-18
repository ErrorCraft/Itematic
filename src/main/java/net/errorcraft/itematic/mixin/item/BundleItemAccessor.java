package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {
    @Accessor("ITEMS_KEY")
    static String itemsKey() {
        throw new AssertionError();
    }

    @Accessor("BUNDLE_ITEM_OCCUPANCY")
    static int itemHolderOccupancy() {
        throw new AssertionError();
    }

    @Accessor("ITEM_BAR_COLOR")
    static int itemBarColor() {
        throw new AssertionError();
    }
}
