package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ActionTypeKeys {
    public static final RegistryKey<ActionType<?>> MODIFY_ITEM = of("modify_item");
    public static final RegistryKey<ActionType<?>> RUN_FUNCTION = of("run_function");
    public static final RegistryKey<ActionType<?>> TELEPORT = of("teleport");
    public static final RegistryKey<ActionType<?>> FERTILIZE = of("fertilize");
    public static final RegistryKey<ActionType<?>> CLEAR_STATUS_EFFECTS = of("clear_status_effects");
    public static final RegistryKey<ActionType<?>> START_USING_ITEM = of("start_using_item");
    public static final RegistryKey<ActionType<?>> EXCHANGE_ITEM = of("exchange_item");
    public static final RegistryKey<ActionType<?>> MODIFY_BLOCK_STATE = of("modify_block_state");
    public static final RegistryKey<ActionType<?>> SEQUENCE = of("sequence");
    public static final RegistryKey<ActionType<?>> PLACE_BLOCK = of("place_block");
    public static final RegistryKey<ActionType<?>> DAMAGE_ITEM = of("damage_item");
    public static final RegistryKey<ActionType<?>> PRIME_TNT = of("prime_tnt");
    public static final RegistryKey<ActionType<?>> SWING_HAND = of("swing_hand");
    public static final RegistryKey<ActionType<?>> MODIFY_SIGN = of("modify_sign");
    public static final RegistryKey<ActionType<?>> WAX_BLOCK = of("wax_block");
    public static final RegistryKey<ActionType<?>> DECREMENT_ITEM = of("decrement_item");
    public static final RegistryKey<ActionType<?>> SET_ITEM_POINTER_LOCATION = of("set_item_pointer_location");
    public static final RegistryKey<ActionType<?>> LIGHT_END_PORTAL = of("light_end_portal");
    public static final RegistryKey<ActionType<?>> PLAY_SOUND = of("play_sound");
    public static final RegistryKey<ActionType<?>> DISPLAY_PARTICLE = of("display_particle");
    public static final RegistryKey<ActionType<?>> SET_BLOCK_STATE = of("set_block_state");
    public static final RegistryKey<ActionType<?>> DROP_ITEM_FROM_BLOCK = of("drop_item_from_block");
    public static final RegistryKey<ActionType<?>> ATTACH_LEASHED_ENTITIES_ON_BLOCK = of("attach_leashed_entities_on_block");
    public static final RegistryKey<ActionType<?>> SET_ENTITY_NAME_FROM_ITEM = of("set_entity_name_from_item");
    public static final RegistryKey<ActionType<?>> OPEN_BOOK_FROM_ITEM = of("open_book_from_item");

    private ActionTypeKeys() {}

    private static RegistryKey<ActionType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ACTION_TYPE, new Identifier(id));
    }
}
