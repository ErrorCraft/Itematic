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

    private ActionTypes() {}

    public static void init() {}

    private static <T extends Action> ActionType<T> register(RegistryKey<ActionType<?>> id, ActionType<T> actionType) {
        return Registry.register(ItematicRegistries.ACTION_TYPE, id, actionType);
    }
}
