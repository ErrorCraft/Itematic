package net.errorcraft.itematic.world.entity.raid;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

public class ItematicRaids {
    private ItematicRaids() {}

    public static ItemStack ominousBanner(ItemStack stack, HolderGetter<BannerPattern> bannerPatterns) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        stack.applyComponents(Raid.getBannerComponentPatch(bannerPatterns));
        return stack;
    }
}
