package net.errorcraft.itematic.item.event;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ItemEventKeys {
    public static final ResourceKey<ItemEvent> USE = of("use");
    public static final ResourceKey<ItemEvent> STOPPED_USING = of("stopped_using");
    public static final ResourceKey<ItemEvent> FINISHED_USING = of("finished_using");
    public static final ResourceKey<ItemEvent> USE_ON_BLOCK = of("use_on_block");
    public static final ResourceKey<ItemEvent> BROKE_BLOCK = of("broke_block");
    public static final ResourceKey<ItemEvent> USE_ON_ENTITY = of("use_on_entity");
    public static final ResourceKey<ItemEvent> HIT_ENTITY = of("hit_entity");
    public static final ResourceKey<ItemEvent> EAT_ITEM = of("eat_item");
    public static final ResourceKey<ItemEvent> PLACED_BLOCK = of("placed_block");
    public static final ResourceKey<ItemEvent> USE_TOOL = of("use_tool");
    public static final ResourceKey<ItemEvent> DAMAGE_ITEM = of("damage_item");
    public static final ResourceKey<ItemEvent> BREAK_ITEM = of("break_item");
    public static final ResourceKey<ItemEvent> SPAWN_ENTITY = of("spawn_entity");
    public static final ResourceKey<ItemEvent> USE_WEAPON = of("use_weapon");
    public static final ResourceKey<ItemEvent> EQUIP_ITEM = of("equip_item");
    public static final ResourceKey<ItemEvent> CONSUME_ITEM = of("consume_item");
    public static final ResourceKey<ItemEvent> THROW_PROJECTILE = of("throw_projectile");
    public static final ResourceKey<ItemEvent> BEFORE_DEATH_HOLDER = of("before_death_holder");

    private ItemEventKeys() {}

    private static ResourceKey<ItemEvent> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.ITEM_EVENT, Identifier.withDefaultNamespace(id));
    }
}
