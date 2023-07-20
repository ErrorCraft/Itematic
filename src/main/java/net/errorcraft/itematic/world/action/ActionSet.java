package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.loot.condition.LootConditionUtil;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;

import java.util.List;
import java.util.Optional;

public record ActionSet(ActionContextParameters context, Optional<LootCondition> conditions, List<Action> actions) {
    public static final Codec<ActionSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameters.CODEC.fieldOf("context").forGetter(ActionSet::context),
        LootConditionUtil.CODEC.optionalFieldOf("conditions").forGetter(ActionSet::conditions),
        Action.CODEC.listOf().fieldOf("actions").forGetter(ActionSet::actions)
    ).apply(instance, ActionSet::new));

    public void execute(ActionContext.Builder builder) {
        LootContext lootContext = builder.createLootContext(this.context);
        if (!this.test(lootContext)) {
            return;
        }
        ActionContext context = builder.build(this.context);
        for (Action action : this.actions) {
            action.execute(context);
        }
    }

    private boolean test(LootContext context) {
        return this.conditions.map(condition -> {
            context.markActive(LootContext.predicate(condition));
            return condition.test(context);
        }).orElse(true);
    }

    public static ActionSet simple(Action... actions) {
        return new ActionSet(new ActionContextParameters(ActionContextParameter.THIS, ActionContextParameter.THIS), Optional.empty(), List.of(actions));
    }

    public static ActionSet targetPosition(Action... actions) {
        return new ActionSet(new ActionContextParameters(ActionContextParameter.THIS, ActionContextParameter.TARGET), Optional.empty(), List.of(actions));
    }
}
