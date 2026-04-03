package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.actions.*;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ActionTypes {
    public static final ActionType<ModifyItemAction> MODIFY_ITEM = register(ActionTypeKeys.MODIFY_ITEM, new ActionType<>(ModifyItemAction.CODEC));
    public static final ActionType<RunFunctionAction> RUN_FUNCTION = register(ActionTypeKeys.RUN_FUNCTION, new ActionType<>(RunFunctionAction.CODEC));
    public static final ActionType<TeleportAction> TELEPORT = register(ActionTypeKeys.TELEPORT, new ActionType<>(TeleportAction.CODEC));
    public static final ActionType<FertilizeAction> FERTILIZE = register(ActionTypeKeys.FERTILIZE, new ActionType<>(FertilizeAction.CODEC));
    public static final ActionType<ClearStatusEffectsAction> CLEAR_STATUS_EFFECTS = register(ActionTypeKeys.CLEAR_STATUS_EFFECTS, new ActionType<>(ClearStatusEffectsAction.CODEC));
    public static final ActionType<ExchangeItemAction> EXCHANGE_ITEM = register(ActionTypeKeys.EXCHANGE_ITEM, new ActionType<>(ExchangeItemAction.CODEC));
    public static final ActionType<ModifyBlockStateAction> MODIFY_BLOCK_STATE = register(ActionTypeKeys.MODIFY_BLOCK_STATE, new ActionType<>(ModifyBlockStateAction.CODEC));
    public static final ActionType<SequenceAction> SEQUENCE = register(ActionTypeKeys.SEQUENCE, new ActionType<>(SequenceAction.CODEC));
    public static final ActionType<PlaceBlockAction> PLACE_BLOCK = register(ActionTypeKeys.PLACE_BLOCK, new ActionType<>(PlaceBlockAction.CODEC));
    public static final ActionType<DamageItemAction> DAMAGE_ITEM = register(ActionTypeKeys.DAMAGE_ITEM, new ActionType<>(DamageItemAction.CODEC));
    public static final ActionType<PrimeTntAction> PRIME_TNT = register(ActionTypeKeys.PRIME_TNT, new ActionType<>(PrimeTntAction.CODEC));
    public static final ActionType<SwingHandAction> SWING_HAND = register(ActionTypeKeys.SWING_HAND, new ActionType<>(SwingHandAction.CODEC));
    public static final ActionType<ModifySignAction> MODIFY_SIGN = register(ActionTypeKeys.MODIFY_SIGN, new ActionType<>(ModifySignAction.CODEC));
    public static final ActionType<WaxBlockAction> WAX_BLOCK = register(ActionTypeKeys.WAX_BLOCK, new ActionType<>(WaxBlockAction.CODEC));
    public static final ActionType<DecrementItemAction> DECREMENT_ITEM = register(ActionTypeKeys.DECREMENT_ITEM, new ActionType<>(DecrementItemAction.CODEC));
    public static final ActionType<SetItemPointerLocationAction> SET_ITEM_POINTER_LOCATION = register(ActionTypeKeys.SET_ITEM_POINTER_LOCATION, new ActionType<>(SetItemPointerLocationAction.CODEC));
    public static final ActionType<LightEndPortalAction> LIGHT_END_PORTAL = register(ActionTypeKeys.LIGHT_END_PORTAL, new ActionType<>(LightEndPortalAction.CODEC));
    public static final ActionType<PlaySoundAction> PLAY_SOUND = register(ActionTypeKeys.PLAY_SOUND, new ActionType<>(PlaySoundAction.CODEC));
    public static final ActionType<DisplayParticleAction> DISPLAY_PARTICLE = register(ActionTypeKeys.DISPLAY_PARTICLE, new ActionType<>(DisplayParticleAction.CODEC));
    public static final ActionType<SetBlockStateAction> SET_BLOCK_STATE = register(ActionTypeKeys.SET_BLOCK_STATE, new ActionType<>(SetBlockStateAction.CODEC));
    public static final ActionType<DropItemFromBlockAction> DROP_ITEM_FROM_BLOCK = register(ActionTypeKeys.DROP_ITEM_FROM_BLOCK, new ActionType<>(DropItemFromBlockAction.CODEC));
    public static final ActionType<AttachLeashedEntitiesOnBlockAction> ATTACH_LEASHED_ENTITIES_ON_BLOCK = register(ActionTypeKeys.ATTACH_LEASHED_ENTITIES_ON_BLOCK, new ActionType<>(AttachLeashedEntitiesOnBlockAction.CODEC));
    public static final ActionType<SetEntityNameFromItemAction> SET_ENTITY_NAME_FROM_ITEM = register(ActionTypeKeys.SET_ENTITY_NAME_FROM_ITEM, new ActionType<>(SetEntityNameFromItemAction.CODEC));
    public static final ActionType<OpenBookFromItemAction> OPEN_BOOK_FROM_ITEM = register(ActionTypeKeys.OPEN_BOOK_FROM_ITEM, new ActionType<>(OpenBookFromItemAction.CODEC));
    public static final ActionType<PlaceBlockFromItemAction> PLACE_BLOCK_FROM_ITEM = register(ActionTypeKeys.PLACE_BLOCK_FROM_ITEM, new ActionType<>(PlaceBlockFromItemAction.CODEC));
    public static final ActionType<MarkBannerOnItemAction> MARK_BANNER_ON_ITEM = register(ActionTypeKeys.MARK_BANNER_ON_ITEM, new ActionType<>(MarkBannerOnItemAction.CODEC));
    public static final ActionType<TwirlPlayerAction> TWIRL_PLAYER = register(ActionTypeKeys.TWIRL_PLAYER, new ActionType<>(TwirlPlayerAction.CODEC));
    public static final ActionType<ApplySuspiciousStewEffectsFromItemAction> APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM = register(ActionTypeKeys.APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM, new ActionType<>(ApplySuspiciousStewEffectsFromItemAction.CODEC));
    public static final ActionType<BrushArmadilloAtPositionAction> BRUSH_ARMADILLO_AT_POSITION = register(ActionTypeKeys.BRUSH_ARMADILLO_AT_POSITION, new ActionType<>(BrushArmadilloAtPositionAction.CODEC));
    public static final ActionType<ChargeRespawnAnchorAction> CHARGE_RESPAWN_ANCHOR = register(ActionTypeKeys.CHARGE_RESPAWN_ANCHOR, new ActionType<>(ChargeRespawnAnchorAction.CODEC));
    public static final ActionType<EquipEntityAtPositionAction> EQUIP_ENTITY_AT_POSITION = register(ActionTypeKeys.EQUIP_ENTITY_AT_POSITION, new ActionType<>(EquipEntityAtPositionAction.CODEC));
    public static final ActionType<EquipHorseWithChestAtPositionAction> EQUIP_HORSE_WITH_CHEST_AT_POSITION = register(ActionTypeKeys.EQUIP_HORSE_WITH_CHEST_AT_POSITION, new ActionType<>(EquipHorseWithChestAtPositionAction.CODEC));
    public static final ActionType<InvokeGameEventAction> INVOKE_GAME_EVENT = register(ActionTypeKeys.INVOKE_GAME_EVENT, new ActionType<>(InvokeGameEventAction.CODEC));
    public static final ActionType<InvokeItemEventAction> INVOKE_ITEM_EVENT = register(ActionTypeKeys.INVOKE_ITEM_EVENT, new ActionType<>(InvokeItemEventAction.CODEC));
    public static final ActionType<PlaceCarvedPumpkinAction> PLACE_CARVED_PUMPKIN = register(ActionTypeKeys.PLACE_CARVED_PUMPKIN, new ActionType<>(PlaceCarvedPumpkinAction.CODEC));
    public static final ActionType<ShearAtPositionAction> SHEAR_AT_POSITION = register(ActionTypeKeys.SHEAR_AT_POSITION, new ActionType<>(ShearAtPositionAction.CODEC));
    public static final ActionType<ShootProjectileFromItemAction> SHOOT_PROJECTILE_FROM_ITEM = register(ActionTypeKeys.SHOOT_PROJECTILE_FROM_ITEM, new ActionType<>(ShootProjectileFromItemAction.CODEC));
    public static final ActionType<SpawnEntityAction> SPAWN_ENTITY = register(ActionTypeKeys.SPAWN_ENTITY, new ActionType<>(SpawnEntityAction.CODEC));
    public static final ActionType<SpawnEntityFromItemAction> SPAWN_ENTITY_FROM_ITEM = register(ActionTypeKeys.SPAWN_ENTITY_FROM_ITEM, new ActionType<>(SpawnEntityFromItemAction.CODEC));
    public static final ActionType<TakeHoneyAction> TAKE_HONEY = register(ActionTypeKeys.TAKE_HONEY, new ActionType<>(TakeHoneyAction.CODEC));
    public static final ActionType<UseBucketAction> USE_BUCKET = register(ActionTypeKeys.USE_BUCKET, new ActionType<>(UseBucketAction.CODEC));
    public static final ActionType<RemoveStatusEffectsAction> REMOVE_STATUS_EFFECTS = register(ActionTypeKeys.REMOVE_STATUS_EFFECTS, new ActionType<>(RemoveStatusEffectsAction.CODEC));
    public static final ActionType<IncrementStatAction> INCREMENT_STAT = register(ActionTypeKeys.INCREMENT_STAT, new ActionType<>(IncrementStatAction.CODEC));
    public static final ActionType<AddStatusEffectsAction> ADD_STATUS_EFFECTS = register(ActionTypeKeys.ADD_STATUS_EFFECTS, new ActionType<>(AddStatusEffectsAction.CODEC));

    private ActionTypes() {}

    public static void init() {}

    private static <T extends Action<T>> ActionType<T> register(RegistryKey<ActionType<?>> id, ActionType<T> actionType) {
        return Registry.register(ItematicRegistries.ACTION_TYPE, id, actionType);
    }
}
