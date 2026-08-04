package net.errorcraft.itematic.world.action.sequence.handler;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class SequenceHandlerTypeKeys {
    public static final ResourceKey<SequenceHandlerType<?>> UNCHECKED = of("unchecked");
    public static final ResourceKey<SequenceHandlerType<?>> FIRST_TO_PASS_REQUIREMENTS = of("first_to_pass_requirements");
    public static final ResourceKey<SequenceHandlerType<?>> PASSING = of("passing");
    public static final ResourceKey<SequenceHandlerType<?>> FIRST_TO_SUCCEED = of("first_to_succeed");
    public static final ResourceKey<SequenceHandlerType<?>> RANDOMIZE = of("randomize");

    private SequenceHandlerTypeKeys() {}

    private static ResourceKey<SequenceHandlerType<?>> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.SEQUENCE_HANDLER_TYPE, Identifier.withDefaultNamespace(id));
    }
}
