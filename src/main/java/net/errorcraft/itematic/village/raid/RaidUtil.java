package net.errorcraft.itematic.village.raid;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BannerPattern;

public class RaidUtil {
    private static ItemStack ominousBanner = null;

    private RaidUtil() {}

    public static ItemStack ominousBanner() {
        if (ominousBanner == null) {
            return ItemStack.EMPTY;
        }

        ItemStack resultingOminousBanner = ominousBanner;
        ominousBanner = null;
        return resultingOminousBanner;
    }

    public static void createOminousBanner(LevelAccessor world) {
        ominousBanner = world.itematic$createStack(ItemKeys.WHITE_BANNER);
    }

    public static ItemStack getOminousBanner(HolderGetter<Item> items, HolderGetter<BannerPattern> bannerPatterns) {
        ominousBanner = new ItemStack(items.getOrThrow(ItemKeys.WHITE_BANNER));
        return Raid.getOminousBannerInstance(bannerPatterns);
    }
}
