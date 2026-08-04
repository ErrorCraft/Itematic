package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ActionTypeKeys {
    public static final ResourceKey<ActionType<?>> MODIFY_ITEM = of("modify_item");
    public static final ResourceKey<ActionType<?>> RUN_FUNCTION = of("run_function");
    public static final ResourceKey<ActionType<?>> TELEPORT = of("teleport");
    public static final ResourceKey<ActionType<?>> FERTILIZE = of("fertilize");
    public static final ResourceKey<ActionType<?>> CLEAR_STATUS_EFFECTS = of("clear_status_effects");
    public static final ResourceKey<ActionType<?>> EXCHANGE_ITEM = of("exchange_item");
    public static final ResourceKey<ActionType<?>> MODIFY_BLOCK_STATE = of("modify_block_state");
    public static final ResourceKey<ActionType<?>> SEQUENCE = of("sequence");
    public static final ResourceKey<ActionType<?>> PLACE_BLOCK = of("place_block");
    public static final ResourceKey<ActionType<?>> DAMAGE_ITEM = of("damage_item");
    public static final ResourceKey<ActionType<?>> PRIME_TNT = of("prime_tnt");
    public static final ResourceKey<ActionType<?>> SWING_HAND = of("swing_hand");
    public static final ResourceKey<ActionType<?>> MODIFY_SIGN = of("modify_sign");
    public static final ResourceKey<ActionType<?>> WAX_BLOCK = of("wax_block");
    public static final ResourceKey<ActionType<?>> DECREMENT_ITEM = of("decrement_item");
    public static final ResourceKey<ActionType<?>> LIGHT_END_PORTAL = of("light_end_portal");
    public static final ResourceKey<ActionType<?>> PLAY_SOUND = of("play_sound");
    public static final ResourceKey<ActionType<?>> DISPLAY_PARTICLE = of("display_particle");
    public static final ResourceKey<ActionType<?>> SET_BLOCK_STATE = of("set_block_state");
    public static final ResourceKey<ActionType<?>> DROP_ITEM_FROM_BLOCK = of("drop_item_from_block");
    public static final ResourceKey<ActionType<?>> ATTACH_LEASHED_ENTITIES_ON_BLOCK = of("attach_leashed_entities_on_block");
    public static final ResourceKey<ActionType<?>> SET_ENTITY_NAME_FROM_ITEM = of("set_entity_name_from_item");
    public static final ResourceKey<ActionType<?>> PLACE_BLOCK_FROM_ITEM = of("place_block_from_item");
    public static final ResourceKey<ActionType<?>> MARK_BANNER_ON_ITEM = of("mark_banner_on_item");
    public static final ResourceKey<ActionType<?>> TWIRL_PLAYER = of("twirl_player");
    public static final ResourceKey<ActionType<?>> APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM = of("apply_suspicious_stew_effects_from_item");
    public static final ResourceKey<ActionType<?>> BRUSH_ARMADILLO_AT_POSITION = of("brush_armadillo_at_position");
    public static final ResourceKey<ActionType<?>> CHARGE_RESPAWN_ANCHOR = of("charge_respawn_anchor");
    public static final ResourceKey<ActionType<?>> EQUIP_ENTITY_AT_POSITION = of("equip_entity_at_position");
    public static final ResourceKey<ActionType<?>> EQUIP_HORSE_WITH_CHEST_AT_POSITION = of("equip_horse_with_chest_at_position");
    public static final ResourceKey<ActionType<?>> INVOKE_GAME_EVENT = of("invoke_game_event");
    public static final ResourceKey<ActionType<?>> INVOKE_ITEM_EVENT = of("invoke_item_event");
    public static final ResourceKey<ActionType<?>> PLACE_CARVED_PUMPKIN = of("place_carved_pumpkin");
    public static final ResourceKey<ActionType<?>> SHEAR_AT_POSITION = of("shear_at_position");
    public static final ResourceKey<ActionType<?>> SHOOT_PROJECTILE_FROM_ITEM = of("shoot_projectile_from_item");
    public static final ResourceKey<ActionType<?>> SPAWN_ENTITY = of("spawn_entity");
    public static final ResourceKey<ActionType<?>> SPAWN_ENTITY_FROM_ITEM = of("spawn_entity_from_item");
    public static final ResourceKey<ActionType<?>> TAKE_HONEY = of("take_honey");
    public static final ResourceKey<ActionType<?>> USE_BUCKET = of("use_bucket");
    public static final ResourceKey<ActionType<?>> REMOVE_STATUS_EFFECTS = of("remove_status_effects");
    public static final ResourceKey<ActionType<?>> INCREMENT_STAT = of("increment_stat");
    public static final ResourceKey<ActionType<?>> ADD_STATUS_EFFECTS = of("add_status_effects");

    private ActionTypeKeys() {}

    private static ResourceKey<ActionType<?>> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.ACTION_TYPE, Identifier.withDefaultNamespace(id));
    }
}
