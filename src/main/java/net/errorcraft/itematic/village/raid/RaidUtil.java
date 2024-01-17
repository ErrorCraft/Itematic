package net.errorcraft.itematic.village.raid;

import net.errorcraft.itematic.item.ItemKeys;
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
        return ominousBanner;
    }

    public static ItemStack createOminousBanner(WorldAccess world) {
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_BANNER);
        return createOminousBanner(stack);
    }

    public static ItemStack createOminousBanner(RegistryEntryLookup<Item> items) {
        ItemStack stack = new ItemStack(items.getOrThrow(ItemKeys.WHITE_BANNER));
        return createOminousBanner(stack);
    }

    private static ItemStack createOminousBanner(ItemStack stack) {
        ominousBanner = stack;
        Raid.getOminousBanner();
        ominousBanner = null;
        return stack;
    }
}
