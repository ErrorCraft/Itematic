package net.errorcraft.itematic.world.entity.raid;

import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BannerPattern;

public class ItematicRaids {
    private ItematicRaids() {}

    public static ItemStack ominousBanner(ResourceKey<Item> item, LevelAccessor level) {
        return ominousBanner(
            level.itematic$createStack(item),
            level.holderLookup(Registries.BANNER_PATTERN)
        );
    }

    public static ItemStackTemplate ominousBanner(Holder<Item> item, HolderGetter<BannerPattern> bannerPatterns) {
        return ItemStackTemplates.of(
            item,
            Raid.getBannerComponentPatch(bannerPatterns)
        );
    }

    public static ItemStack ominousBanner(ItemStack stack, HolderGetter<BannerPattern> bannerPatterns) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        stack.applyComponents(Raid.getBannerComponentPatch(bannerPatterns));
        return stack;
    }
}
