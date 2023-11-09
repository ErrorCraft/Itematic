package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.util.dynamic.Codecs;

public interface Action {
    Codec<Action> ELEMENT_CODEC = ItematicRegistries.ACTION_TYPE.getCodec().dispatch("type", Action::type, ActionType::codec);
    Codec<Action> CODEC = Codecs.createLazy(() -> ItematicCodecs.alternatively(ELEMENT_CODEC, SequenceAction.INLINE_CODEC, action -> {
        if (action instanceof SequenceAction sequenceAction && sequenceAction.isSimple()) {
            return sequenceAction;
        }
        return null;
    }));

    ActionType<?> type();
    boolean execute(ActionContext context);
}
