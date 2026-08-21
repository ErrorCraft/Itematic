package net.errorcraft.itematic.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ItematicItemTags {
    public static final TagKey<Item> BOW_AMMUNITION = of("bow_ammunition");
    public static final TagKey<Item> CROSSBOW_AMMUNITION = of("crossbow_ammunition");
    public static final TagKey<Item> PIG_TEMPT_ITEMS = of("pig_tempt_items");
    public static final TagKey<Item> VILLAGER_GATHERABLE_ITEMS = of("villager_gatherable_items");
    public static final TagKey<Item> FARMER_VILLAGER_GATHERABLE_ITEMS = of("farmer_villager_gatherable_items");
    public static final TagKey<Item> BANNED_BUNDLE_ITEMS = of("banned_bundle_items");
    public static final TagKey<Item> SHULKER_BOXES = of("shulker_boxes");
    public static final TagKey<Item> PREVENTS_TAKING_POTTED_ITEM_OUT = of("prevents_taking_potted_item_out");
    public static final TagKey<Item> BREWING_INPUTS = of("brewing_inputs");
    public static final TagKey<Item> MUNDANE_POTION_REAGENTS = of("mundane_potion_reagents");

    private ItematicItemTags() {}

    private static TagKey<Item> of(String id) {
        return TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace(id));
    }
}
