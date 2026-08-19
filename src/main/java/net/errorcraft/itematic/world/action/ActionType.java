package net.errorcraft.itematic.world.action;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.action.actions.*;
import net.minecraft.core.Registry;

public record ActionType<T extends Action<T>>(MapCodec<T> codec) {
    public static final ActionType<ModifyItemAction> MODIFY_ITEM = register(
        "modify_item",
        new ActionType<>(ModifyItemAction.CODEC)
    );
    public static final ActionType<RunFunctionAction> RUN_FUNCTION = register(
        "run_function",
        new ActionType<>(RunFunctionAction.CODEC)
    );
    public static final ActionType<TeleportAction> TELEPORT = register(
        "teleport",
        new ActionType<>(TeleportAction.CODEC)
    );
    public static final ActionType<FertilizeAction> FERTILIZE = register(
        "fertilize",
        new ActionType<>(FertilizeAction.CODEC)
    );
    public static final ActionType<ClearStatusEffectsAction> CLEAR_STATUS_EFFECTS = register(
        "clear_status_effects",
        new ActionType<>(ClearStatusEffectsAction.CODEC)
    );
    public static final ActionType<ExchangeItemAction> EXCHANGE_ITEM = register(
        "exchange_item",
        new ActionType<>(ExchangeItemAction.CODEC)
    );
    public static final ActionType<ModifyBlockStateAction> MODIFY_BLOCK_STATE = register(
        "modify_block_state",
        new ActionType<>(ModifyBlockStateAction.CODEC)
    );
    public static final ActionType<SequenceAction> SEQUENCE = register(
        "sequence",
        new ActionType<>(SequenceAction.CODEC)
    );
    public static final ActionType<PlaceBlockAction> PLACE_BLOCK = register(
        "place_block",
        new ActionType<>(PlaceBlockAction.CODEC)
    );
    public static final ActionType<DamageItemAction> DAMAGE_ITEM = register(
        "damage_item",
        new ActionType<>(DamageItemAction.CODEC)
    );
    public static final ActionType<PrimeTntAction> PRIME_TNT = register(
        "prime_tnt",
        new ActionType<>(PrimeTntAction.CODEC)
    );
    public static final ActionType<SwingHandAction> SWING_HAND = register(
        "swing_hand",
        new ActionType<>(SwingHandAction.CODEC)
    );
    public static final ActionType<ModifySignAction> MODIFY_SIGN = register(
        "modify_sign",
        new ActionType<>(ModifySignAction.CODEC)
    );
    public static final ActionType<WaxBlockAction> WAX_BLOCK = register(
        "wax_block",
        new ActionType<>(WaxBlockAction.CODEC)
    );
    public static final ActionType<DecrementItemAction> DECREMENT_ITEM = register(
        "decrement_item",
        new ActionType<>(DecrementItemAction.CODEC)
    );
    public static final ActionType<LightEndPortalAction> LIGHT_END_PORTAL = register(
        "light_end_portal",
        new ActionType<>(LightEndPortalAction.CODEC)
    );
    public static final ActionType<PlaySoundAction> PLAY_SOUND = register(
        "play_sound",
        new ActionType<>(PlaySoundAction.CODEC)
    );
    public static final ActionType<DisplayParticleAction> DISPLAY_PARTICLE = register(
        "display_particle",
        new ActionType<>(DisplayParticleAction.CODEC)
    );
    public static final ActionType<SetBlockStateAction> SET_BLOCK_STATE = register(
        "set_block_state",
        new ActionType<>(SetBlockStateAction.CODEC)
    );
    public static final ActionType<DropItemFromBlockAction> DROP_ITEM_FROM_BLOCK = register(
        "drop_item_from_block",
        new ActionType<>(DropItemFromBlockAction.CODEC)
    );
    public static final ActionType<AttachLeashedEntitiesOnBlockAction> ATTACH_LEASHED_ENTITIES_ON_BLOCK = register(
        "attach_leashed_entities_on_block",
        new ActionType<>(AttachLeashedEntitiesOnBlockAction.CODEC)
    );
    public static final ActionType<SetEntityNameFromItemAction> SET_ENTITY_NAME_FROM_ITEM = register(
        "set_entity_name_from_item",
        new ActionType<>(SetEntityNameFromItemAction.CODEC)
    );
    public static final ActionType<PlaceBlockFromItemAction> PLACE_BLOCK_FROM_ITEM = register(
        "place_block_from_item",
        new ActionType<>(PlaceBlockFromItemAction.CODEC)
    );
    public static final ActionType<MarkBannerOnItemAction> MARK_BANNER_ON_ITEM = register(
        "mark_banner_on_item",
        new ActionType<>(MarkBannerOnItemAction.CODEC)
    );
    public static final ActionType<TwirlPlayerAction> TWIRL_PLAYER = register(
        "twirl_player",
        new ActionType<>(TwirlPlayerAction.CODEC)
    );
    public static final ActionType<ApplySuspiciousStewEffectsFromItemAction> APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM = register(
        "apply_suspicious_stew_effects_from_item",
        new ActionType<>(ApplySuspiciousStewEffectsFromItemAction.CODEC)
    );
    public static final ActionType<BrushArmadilloAtPositionAction> BRUSH_ARMADILLO_AT_POSITION = register(
        "brush_armadillo_at_position",
        new ActionType<>(BrushArmadilloAtPositionAction.CODEC)
    );
    public static final ActionType<ChargeRespawnAnchorAction> CHARGE_RESPAWN_ANCHOR = register(
        "charge_respawn_anchor",
        new ActionType<>(ChargeRespawnAnchorAction.CODEC)
    );
    public static final ActionType<EquipEntityAtPositionAction> EQUIP_ENTITY_AT_POSITION = register(
        "equip_entity_at_position",
        new ActionType<>(EquipEntityAtPositionAction.CODEC)
    );
    public static final ActionType<EquipHorseWithChestAtPositionAction> EQUIP_HORSE_WITH_CHEST_AT_POSITION = register(
        "equip_horse_with_chest_at_position",
        new ActionType<>(EquipHorseWithChestAtPositionAction.CODEC)
    );
    public static final ActionType<InvokeGameEventAction> INVOKE_GAME_EVENT = register(
        "invoke_game_event",
        new ActionType<>(InvokeGameEventAction.CODEC)
    );
    public static final ActionType<InvokeItemEventAction> INVOKE_ITEM_EVENT = register(
        "invoke_item_event",
        new ActionType<>(InvokeItemEventAction.CODEC)
    );
    public static final ActionType<PlaceCarvedPumpkinAction> PLACE_CARVED_PUMPKIN = register(
        "place_carved_pumpkin",
        new ActionType<>(PlaceCarvedPumpkinAction.CODEC)
    );
    public static final ActionType<ShearAtPositionAction> SHEAR_AT_POSITION = register(
        "shear_at_position",
        new ActionType<>(ShearAtPositionAction.CODEC)
    );
    public static final ActionType<ShootProjectileFromItemAction> SHOOT_PROJECTILE_FROM_ITEM = register(
        "shoot_projectile_from_item",
        new ActionType<>(ShootProjectileFromItemAction.CODEC)
    );
    public static final ActionType<SpawnEntityAction> SPAWN_ENTITY = register(
        "spawn_entity",
        new ActionType<>(SpawnEntityAction.CODEC)
    );
    public static final ActionType<SpawnEntityFromItemAction> SPAWN_ENTITY_FROM_ITEM = register(
        "spawn_entity_from_item",
        new ActionType<>(SpawnEntityFromItemAction.CODEC)
    );
    public static final ActionType<TakeHoneyAction> TAKE_HONEY = register(
        "take_honey",
        new ActionType<>(TakeHoneyAction.CODEC)
    );
    public static final ActionType<UseBucketAction> USE_BUCKET = register(
        "use_bucket",
        new ActionType<>(UseBucketAction.CODEC)
    );
    public static final ActionType<RemoveStatusEffectsAction> REMOVE_STATUS_EFFECTS = register(
        "remove_status_effects",
        new ActionType<>(RemoveStatusEffectsAction.CODEC)
    );
    public static final ActionType<IncrementStatAction> INCREMENT_STAT = register(
        "increment_stat",
        new ActionType<>(IncrementStatAction.CODEC)
    );
    public static final ActionType<AddStatusEffectsAction> ADD_STATUS_EFFECTS = register(
        "add_status_effects",
        new ActionType<>(AddStatusEffectsAction.CODEC)
    );

    public static void init() {}

    private static <T extends Action<T>> ActionType<T> register(String id, ActionType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.ACTION_TYPE, id, type);
    }
}
