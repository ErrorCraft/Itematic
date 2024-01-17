package net.errorcraft.itematic.entity.passive;

import com.google.common.collect.ImmutableMap;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

import java.util.Map;

public class VillagerEntityUtil {
    public static final Map<RegistryKey<Item>, Integer> ITEM_FOOD_POINTS = ImmutableMap.of(
        ItemKeys.BREAD, 4,
        ItemKeys.POTATO, 1,
        ItemKeys.CARROT, 1,
        ItemKeys.BEETROOT, 1
    );

    private VillagerEntityUtil() {}
}
