package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ActionTypeKeys {
    public static final RegistryKey<ActionType<?>> MODIFY_ITEM = of("modify_item");
    public static final RegistryKey<ActionType<?>> RUN_FUNCTION = of("run_function");
    public static final RegistryKey<ActionType<?>> TELEPORT = of("teleport");
    public static final RegistryKey<ActionType<?>> FERTILIZE = of("fertilize");

    private ActionTypeKeys() {}

    private static RegistryKey<ActionType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ACTION_TYPE, new Identifier(id));
    }
}
