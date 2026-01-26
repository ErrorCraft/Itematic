package net.errorcraft.itematic.village.raid;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.WorldAccess;

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

    public static void createOminousBanner(WorldAccess world) {
        ominousBanner = world.itematic$createStack(ItemKeys.WHITE_BANNER);
    }

    public static ItemStack getOminousBanner(RegistryEntryLookup<Item> items, RegistryEntryLookup<BannerPattern> bannerPatterns) {
        ominousBanner = new ItemStack(items.getOrThrow(ItemKeys.WHITE_BANNER));
        return Raid.getOminousBanner(bannerPatterns);
    }
}
