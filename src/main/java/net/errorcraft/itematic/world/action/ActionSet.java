package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;

import java.util.List;
import java.util.Optional;

public record ActionSet(List<Action> actions, Optional<ActionRequirements> requirements) {
    public static final Codec<ActionSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Action.CODEC.listOf().fieldOf("actions").forGetter(ActionSet::actions),
        ActionRequirements.CODEC.optionalFieldOf("requirements").forGetter(ActionSet::requirements)
    ).apply(instance, ActionSet::new));

    public void execute(ActionContext context) {
        if (!this.test(context)) {
            return;
        }
        for (Action action : this.actions) {
            action.execute(context);
        }
    }

    private boolean test(ActionContext context) {
        return this.requirements.map(requirements -> requirements.test(context))
            .orElse(true);
    }

    public static ActionSet simple(Action... actions) {
        return new ActionSet(List.of(actions), Optional.empty());
    }
}
