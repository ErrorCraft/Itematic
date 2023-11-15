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
    public static final ActionType<StartUsingItemAction> START_USING_ITEM = register(ActionTypeKeys.START_USING_ITEM, new ActionType<>(StartUsingItemAction.CODEC));
    public static final ActionType<ExchangeItemAction> EXCHANGE_ITEM = register(ActionTypeKeys.EXCHANGE_ITEM, new ActionType<>(ExchangeItemAction.CODEC));
    public static final ActionType<ModifyBlockStateAction> MODIFY_BLOCK_STATE = register(ActionTypeKeys.MODIFY_BLOCK_STATE, new ActionType<>(ModifyBlockStateAction.CODEC));
    public static final ActionType<SequenceAction> SEQUENCE = register(ActionTypeKeys.SEQUENCE, new ActionType<>(SequenceAction.CODEC));
    public static final ActionType<FirstToPassAction> FIRST_TO_PASS = register(ActionTypeKeys.FIRST_TO_PASS, new ActionType<>(FirstToPassAction.CODEC));
    public static final ActionType<PlaceBlockAction> PLACE_BLOCK = register(ActionTypeKeys.PLACE_BLOCK, new ActionType<>(PlaceBlockAction.CODEC));
    public static final ActionType<DamageItemAction> DAMAGE_ITEM = register(ActionTypeKeys.DAMAGE_ITEM, new ActionType<>(DamageItemAction.CODEC));
    public static final ActionType<PrimeTntAction> PRIME_TNT = register(ActionTypeKeys.PRIME_TNT, new ActionType<>(PrimeTntAction.CODEC));
    public static final ActionType<SwingHandAction> SWING_HAND = register(ActionTypeKeys.SWING_HAND, new ActionType<>(SwingHandAction.CODEC));
    public static final ActionType<ModifySignAction> MODIFY_SIGN = register(ActionTypeKeys.MODIFY_SIGN, new ActionType<>(ModifySignAction.CODEC));
    public static final ActionType<WaxBlockAction> WAX_BLOCK = register(ActionTypeKeys.WAX_BLOCK, new ActionType<>(WaxBlockAction.CODEC));
    public static final ActionType<DecrementItemAction> DECREMENT_ITEM = register(ActionTypeKeys.DECREMENT_ITEM, new ActionType<>(DecrementItemAction.CODEC));

    private ActionTypes() {}

    public static void init() {}

    private static <T extends Action> ActionType<T> register(RegistryKey<ActionType<?>> id, ActionType<T> actionType) {
        return Registry.register(ItematicRegistries.ACTION_TYPE, id, actionType);
    }
}
