package net.errorcraft.itematic.world.action.sequence.handler;

import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class SequenceHandlerTypes {
    public static final SequenceHandlerType<UncheckedSequenceHandler> UNCHECKED = register(SequenceHandlerTypeKeys.UNCHECKED, new SequenceHandlerType<>(UncheckedSequenceHandler.CODEC));
    public static final SequenceHandlerType<FirstToPassRequirementsSequenceHandler> FIRST_TO_PASS_REQUIREMENTS = register(SequenceHandlerTypeKeys.FIRST_TO_PASS_REQUIREMENTS, new SequenceHandlerType<>(FirstToPassRequirementsSequenceHandler.CODEC));
    public static final SequenceHandlerType<PassingSequenceHandler> PASSING = register(SequenceHandlerTypeKeys.PASSING, new SequenceHandlerType<>(PassingSequenceHandler.CODEC));

    private SequenceHandlerTypes() {}

    public static void init() {}

    private static <T extends SequenceHandler> SequenceHandlerType<T> register(RegistryKey<SequenceHandlerType<?>> id, SequenceHandlerType<T> type) {
        return Registry.register(ItematicRegistries.SEQUENCE_HANDLER_TYPE, id, type);
    }
}
