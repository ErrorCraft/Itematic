package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.actions.PassingSequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;

import java.util.Optional;

public record ActionEntry(Action action, Optional<ActionRequirements> requirements) {
    public static final Codec<ActionEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Action.CODEC.fieldOf("action").forGetter(ActionEntry::action),
        ActionRequirements.CODEC.optionalFieldOf("requirements").forGetter(ActionEntry::requirements)
    ).apply(instance, ActionEntry::new));

    public Optional<Boolean> execute(ActionContext context) {
        if (!this.test(context)) {
            return Optional.empty();
        }
        return Optional.of(this.action.execute(context));
    }

    private boolean test(ActionContext context) {
        return this.requirements.map(requirements -> requirements.test(context))
            .orElse(true);
    }

    public static ActionEntry simple(Action action) {
        return new ActionEntry(action, Optional.empty());
    }

    public static ActionEntry simple(ActionRequirements requirements, Action action) {
        return new ActionEntry(action, Optional.of(requirements));
    }

    public static ActionEntry passing(Action... actions) {
        return simple(PassingSequenceAction.of(actions));
    }

    public static ActionEntry passing(ActionRequirements requirements, Action... actions) {
        return simple(requirements, PassingSequenceAction.of(actions));
    }
}
