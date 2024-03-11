package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {
    @Accessor("ITEM_BAR_COLOR")
    static int itemBarColor() {
        throw new AssertionError();
    }

    @Invoker("dropAllBundledItems")
    static boolean dropAllBundledItems(ItemStack stack, PlayerEntity player) {
        throw new AssertionError();
    }
}
