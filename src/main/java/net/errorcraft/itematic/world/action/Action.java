package net.errorcraft.itematic.world.action;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.util.dynamic.Codecs;

public interface Action<T extends Action<T>> {
    Codec<Action<?>> ELEMENT_CODEC = ItematicRegistries.ACTION_TYPE.getCodec().dispatch("type", Action::type, ActionType::codec);
    Codec<Action<?>> CODEC = Codecs.createLazy(() -> Codecs.either(ELEMENT_CODEC, UncheckedSequenceHandler.CODEC).xmap(either -> either.map(action -> action, SequenceAction::new), action -> {
        if (action instanceof SequenceAction sequenceAction && sequenceAction.handler() instanceof UncheckedSequenceHandler uncheckedSequenceHandler) {
            return Either.right(uncheckedSequenceHandler);
        }
        return Either.left(action);
    }));

    ActionType<T> type();
    boolean execute(ActionContext context);
}
