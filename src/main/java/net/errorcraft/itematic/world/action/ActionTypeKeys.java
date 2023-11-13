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
    public static final RegistryKey<ActionType<?>> FIRST_TO_PASS = of("first_to_pass");
    public static final RegistryKey<ActionType<?>> PLACE_BLOCK = of("place_block");
    public static final RegistryKey<ActionType<?>> DAMAGE_ITEM = of("damage_item");
    public static final RegistryKey<ActionType<?>> PRIME_TNT = of("prime_tnt");
    public static final RegistryKey<ActionType<?>> SWING_HAND = of("swing_hand");

    private ActionTypeKeys() {}

    private static RegistryKey<ActionType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ACTION_TYPE, new Identifier(id));
    }
}
