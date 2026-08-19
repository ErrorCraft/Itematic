package net.errorcraft.itematic.world.entity.raid;

import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BannerPattern;

public class ItematicRaids {
    private static ItemStack ominousBanner = null;

    private ItematicRaids() {}

    public static ItemStack ominousBanner() {
        if (ItemStacks.isNullOrEmpty(ominousBanner)) {
            return ItemStack.EMPTY;
        }

        ItemStack resultingOminousBanner = ominousBanner;
        ominousBanner = null;
        return resultingOminousBanner;
    }

    public static void createOminousBanner(LevelAccessor level) {
        ominousBanner = level.itematic$createStack(ItemIds.WHITE_BANNER);
    }

    public static ItemStack getOminousBanner(HolderGetter<Item> items, HolderGetter<BannerPattern> bannerPatterns) {
        ominousBanner = new ItemStack(items.getOrThrow(ItemIds.WHITE_BANNER));
        return Raid.getOminousBannerInstance(bannerPatterns);
    }
}
