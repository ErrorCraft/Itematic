package net.errorcraft.itematic.world.action.sequence.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToSucceedSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.RandomizeSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.core.Registry;

public record SequenceHandlerType<T extends SequenceHandler<T>>(MapCodec<T> codec) {
    public static final SequenceHandlerType<UncheckedSequenceHandler> UNCHECKED = register(
        "unchecked",
        new SequenceHandlerType<>(UncheckedSequenceHandler.CODEC)
    );
    public static final SequenceHandlerType<FirstToPassRequirementsSequenceHandler> FIRST_TO_PASS_REQUIREMENTS = register(
        "first_to_pass_requirements",
        new SequenceHandlerType<>(FirstToPassRequirementsSequenceHandler.CODEC)
    );
    public static final SequenceHandlerType<PassingSequenceHandler> PASSING = register(
        "passing",
        new SequenceHandlerType<>(PassingSequenceHandler.CODEC)
    );
    public static final SequenceHandlerType<FirstToSucceedSequenceHandler> FIRST_TO_SUCCEED = register(
        "first_to_succeed",
        new SequenceHandlerType<>(FirstToSucceedSequenceHandler.CODEC)
    );
    public static final SequenceHandlerType<RandomizeSequenceHandler> RANDOMIZE = register(
        "randomize",
        new SequenceHandlerType<>(RandomizeSequenceHandler.CODEC)
    );

    public SequenceHandlerType(Codec<T> codec) {
        this(codec.fieldOf("entries"));
    }

    public static void init() {}

    private static <T extends SequenceHandler<T>> SequenceHandlerType<T> register(String id, SequenceHandlerType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.SEQUENCE_HANDLER_TYPE, id, type);
    }
}
