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
    public static final RegistryKey<ActionType<?>> PLACE_BLOCK_FROM_ITEM = of("place_block_from_item");
    public static final RegistryKey<ActionType<?>> MARK_BANNER_ON_ITEM = of("mark_banner_on_item");
    public static final RegistryKey<ActionType<?>> TWIRL_PLAYER = of("twirl_player");
    public static final RegistryKey<ActionType<?>> APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM = of("apply_suspicious_stew_effects_from_item");
    public static final RegistryKey<ActionType<?>> BRUSH_ARMADILLO_AT_POSITION = of("brush_armadillo_at_position");
    public static final RegistryKey<ActionType<?>> CHARGE_RESPAWN_ANCHOR = of("charge_respawn_anchor");
    public static final RegistryKey<ActionType<?>> EQUIP_ENTITY_AT_POSITION = of("equip_entity_at_position");
    public static final RegistryKey<ActionType<?>> EQUIP_HORSE_WITH_CHEST_AT_POSITION = of("equip_horse_with_chest_at_position");
    public static final RegistryKey<ActionType<?>> INVOKE_GAME_EVENT = of("invoke_game_event");
    public static final RegistryKey<ActionType<?>> INVOKE_ITEM_EVENT = of("invoke_item_event");
    public static final RegistryKey<ActionType<?>> PLACE_CARVED_PUMPKIN = of("place_carved_pumpkin");
    public static final RegistryKey<ActionType<?>> SADDLE_ENTITY_AT_POSITION = of("saddle_entity_at_position");
    public static final RegistryKey<ActionType<?>> SHEAR_AT_POSITION = of("shear_at_position");
    public static final RegistryKey<ActionType<?>> SHOOT_PROJECTILE_FROM_ITEM = of("shoot_projectile_from_item");
    public static final RegistryKey<ActionType<?>> SPAWN_ENTITY = of("spawn_entity");
    public static final RegistryKey<ActionType<?>> SPAWN_ENTITY_FROM_ITEM = of("spawn_entity_from_item");
    public static final RegistryKey<ActionType<?>> TAKE_HONEY = of("take_honey");
    public static final RegistryKey<ActionType<?>> USE_BUCKET = of("use_bucket");

    private ActionTypeKeys() {}

    private static RegistryKey<ActionType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ACTION_TYPE, new Identifier(id));
    }
}
