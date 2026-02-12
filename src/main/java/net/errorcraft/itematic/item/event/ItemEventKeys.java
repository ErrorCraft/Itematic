package net.errorcraft.itematic.item.event;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItemEventKeys {
    public static final RegistryKey<ItemEvent> USE = of("use");
    public static final RegistryKey<ItemEvent> STOPPED_USING = of("stopped_using");
    public static final RegistryKey<ItemEvent> FINISHED_USING = of("finished_using");
    public static final RegistryKey<ItemEvent> USE_ON_BLOCK = of("use_on_block");
    public static final RegistryKey<ItemEvent> BROKE_BLOCK = of("broke_block");
    public static final RegistryKey<ItemEvent> USE_ON_ENTITY = of("use_on_entity");
    public static final RegistryKey<ItemEvent> HIT_ENTITY = of("hit_entity");
    public static final RegistryKey<ItemEvent> EAT_ITEM = of("eat_item");
    public static final RegistryKey<ItemEvent> PLACED_BLOCK = of("placed_block");
    public static final RegistryKey<ItemEvent> USE_TOOL = of("use_tool");
    public static final RegistryKey<ItemEvent> DAMAGE_ITEM = of("damage_item");
    public static final RegistryKey<ItemEvent> BREAK_ITEM = of("break_item");
    public static final RegistryKey<ItemEvent> SPAWN_ENTITY = of("spawn_entity");
    public static final RegistryKey<ItemEvent> USE_WEAPON = of("use_weapon");
    public static final RegistryKey<ItemEvent> EQUIP_ITEM = of("equip_item");
    public static final RegistryKey<ItemEvent> CONSUME_ITEM = of("consume_item");
    public static final RegistryKey<ItemEvent> THROW_PROJECTILE = of("throw_projectile");
    public static final RegistryKey<ItemEvent> BEFORE_DEATH_HOLDER = of("before_death_holder");

    private ItemEventKeys() {}

    private static RegistryKey<ItemEvent> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ITEM_EVENT, Identifier.ofVanilla(id));
    }
}
