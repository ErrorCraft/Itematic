package net.errorcraft.itematic.world.entity.npc.villager;

import com.google.common.collect.ImmutableMap;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import java.util.Map;

public class Villagers {
    public static final Map<ResourceKey<Item>, Integer> ITEM_FOOD_POINTS = ImmutableMap.of(
        ItemIds.BREAD, 4,
        ItemIds.POTATO, 1,
        ItemIds.CARROT, 1,
        ItemIds.BEETROOT, 1
    );

    private Villagers() {}
}
