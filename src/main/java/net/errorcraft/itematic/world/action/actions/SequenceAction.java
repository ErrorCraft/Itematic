package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;

import java.util.List;

public record SequenceAction(List<Action> entries) implements Action {
    public static final Codec<SequenceAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Action.CODEC.listOf().fieldOf("entries").forGetter(SequenceAction::entries)
    ).apply(instance, SequenceAction::new));
    public static final Codec<SequenceAction> INLINE_CODEC = Action.CODEC.listOf().xmap(SequenceAction::new, SequenceAction::entries);

    @Override
    public ActionType<?> type() {
        return ActionTypes.SEQUENCE;
    }

    @Override
    public boolean execute(ActionContext context) {
        boolean result = false;
        for (Action entry : this.entries) {
            result |= entry.execute(context);
        }
        return result;
    }

    public static SequenceAction of(Action... actions) {
        return new SequenceAction(List.of(actions));
    }
}
