package net.errorcraft.itematic.world.item;

import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.util.Util;

public record ItemEvent() {
    public static final ItemEvent USE = register("use");
    public static final ItemEvent STOPPED_USING = register("stopped_using");
    public static final ItemEvent FINISHED_USING = register("finished_using");
    public static final ItemEvent BEFORE_USE_ON_BLOCK = register("before_use_on_block");
    public static final ItemEvent USE_ON_BLOCK = register("use_on_block");
    public static final ItemEvent BROKE_BLOCK = register("broke_block");
    public static final ItemEvent BEFORE_USE_ON_ENTITY = register("before_use_on_entity");
    public static final ItemEvent USE_ON_ENTITY = register("use_on_entity");
    public static final ItemEvent HIT_ENTITY = register("hit_entity");
    public static final ItemEvent EAT_ITEM = register("eat_item");
    public static final ItemEvent PLACED_BLOCK = register("placed_block");
    public static final ItemEvent DAMAGE_ITEM = register("damage_item");
    public static final ItemEvent BREAK_ITEM = register("break_item");
    public static final ItemEvent USE_TOOL = register("use_tool");
    public static final ItemEvent SPAWN_ENTITY = register("spawn_entity");
    public static final ItemEvent USE_WEAPON = register("use_weapon");
    public static final ItemEvent EQUIP_ITEM = register("equip_item");
    public static final ItemEvent CONSUME_ITEM = register("consume_item");
    public static final ItemEvent THROW_PROJECTILE = register("throw_projectile");
    public static final ItemEvent BEFORE_DEATH_HOLDER = register("before_death_holder");

    public static void init() {}

    private static ItemEvent register(String id) {
        return Registry.register(ItematicBuiltInRegistries.ITEM_EVENT, id, new ItemEvent());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String toString() {
        return Util.getRegisteredName(ItematicBuiltInRegistries.ITEM_EVENT, this);
    }
}
