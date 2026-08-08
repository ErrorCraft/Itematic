package net.errorcraft.itematic.world.entity.npc.villager;

import com.google.common.collect.ImmutableMap;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import java.util.Map;

public class Villagers {
    public static final Map<ResourceKey<Item>, Integer> ITEM_FOOD_POINTS = ImmutableMap.of(
        ItemKeys.BREAD, 4,
        ItemKeys.POTATO, 1,
        ItemKeys.CARROT, 1,
        ItemKeys.BEETROOT, 1
    );

    private Villagers() {}
}
