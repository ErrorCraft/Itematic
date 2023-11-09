package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;

import java.util.List;

public record SequenceAction(List<Action> entries, boolean actionMustPass) implements Action {
    public static final Codec<SequenceAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Action.CODEC.listOf().fieldOf("entries").forGetter(SequenceAction::entries),
        Codec.BOOL.optionalFieldOf("action_must_pass", false).forGetter(SequenceAction::actionMustPass)
    ).apply(instance, SequenceAction::new));
    public static final Codec<SequenceAction> INLINE_CODEC = Action.CODEC.listOf().xmap(SequenceAction::new, SequenceAction::entries);

    public SequenceAction(List<Action> entries) {
        this(entries, false);
    }

    @Override
    public ActionType<?> type() {
        return ActionTypes.SEQUENCE;
    }

    @Override
    public boolean execute(ActionContext context) {
        for (Action entry : this.entries) {
            if (!entry.execute(context) && this.actionMustPass) {
                return false;
            }
        }
        return true;
    }

    public boolean isSimple() {
        return !this.actionMustPass;
    }

    public static SequenceAction of(Action... actions) {
        return new SequenceAction(List.of(actions));
    }

    public static SequenceAction passing(Action... actions) {
        return new SequenceAction(List.of(actions), true);
    }
}
