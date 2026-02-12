package net.errorcraft.itematic.item.event;

import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ItemEvents {
    public static final ItemEvent USE = register(ItemEventKeys.USE);
    public static final ItemEvent STOPPED_USING = register(ItemEventKeys.STOPPED_USING);
    public static final ItemEvent FINISHED_USING = register(ItemEventKeys.FINISHED_USING);
    public static final ItemEvent USE_ON_BLOCK = register(ItemEventKeys.USE_ON_BLOCK);
    public static final ItemEvent BROKE_BLOCK = register(ItemEventKeys.BROKE_BLOCK);
    public static final ItemEvent USE_ON_ENTITY = register(ItemEventKeys.USE_ON_ENTITY);
    public static final ItemEvent HIT_ENTITY = register(ItemEventKeys.HIT_ENTITY);
    public static final ItemEvent EAT_ITEM = register(ItemEventKeys.EAT_ITEM);
    public static final ItemEvent PLACED_BLOCK = register(ItemEventKeys.PLACED_BLOCK);
    public static final ItemEvent DAMAGE_ITEM = register(ItemEventKeys.DAMAGE_ITEM);
    public static final ItemEvent BREAK_ITEM = register(ItemEventKeys.BREAK_ITEM);
    public static final ItemEvent USE_TOOL = register(ItemEventKeys.USE_TOOL);
    public static final ItemEvent SPAWN_ENTITY = register(ItemEventKeys.SPAWN_ENTITY);
    public static final ItemEvent USE_WEAPON = register(ItemEventKeys.USE_WEAPON);
    public static final ItemEvent EQUIP_ITEM = register(ItemEventKeys.EQUIP_ITEM);
    public static final ItemEvent CONSUME_ITEM = register(ItemEventKeys.CONSUME_ITEM);
    public static final ItemEvent THROW_PROJECTILE = register(ItemEventKeys.THROW_PROJECTILE);
    public static final ItemEvent BEFORE_DEATH_HOLDER = register(ItemEventKeys.BEFORE_DEATH_HOLDER);

    private ItemEvents() {}

    public static void init() {}

    private static ItemEvent register(RegistryKey<ItemEvent> id) {
        return Registry.register(ItematicRegistries.ITEM_EVENT, id, new ItemEvent(id));
    }
}
