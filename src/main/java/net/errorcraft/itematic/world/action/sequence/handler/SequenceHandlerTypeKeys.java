package net.errorcraft.itematic.world.action.sequence.handler;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class SequenceHandlerTypeKeys {
    public static final RegistryKey<SequenceHandlerType<?>> UNCHECKED = of("unchecked");
    public static final RegistryKey<SequenceHandlerType<?>> FIRST_TO_PASS_REQUIREMENTS = of("first_to_pass_requirements");
    public static final RegistryKey<SequenceHandlerType<?>> PASSING = of("passing");
    public static final RegistryKey<SequenceHandlerType<?>> FIRST_TO_SUCCEED = of("first_to_succeed");
    public static final RegistryKey<SequenceHandlerType<?>> RANDOMIZE = of("randomize");

    private SequenceHandlerTypeKeys() {}

    private static RegistryKey<SequenceHandlerType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.SEQUENCE_HANDLER_TYPE, Identifier.ofVanilla(id));
    }
}
