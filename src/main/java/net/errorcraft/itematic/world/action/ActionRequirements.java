package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;

public record ActionRequirements(LootCondition conditions) {
    public static final Codec<ActionRequirements> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        LootCondition.CODEC.fieldOf("conditions").forGetter(ActionRequirements::conditions)
    ).apply(instance, ActionRequirements::new));

    public static ActionRequirements of(ActionContextParameters context, LootCondition conditions) {
        return new ActionRequirements(conditions);
    }

    public boolean test(NewActionContext context) {
        LootContext lootContext = context.lootContext();
        lootContext.markActive(LootContext.predicate(this.conditions));
        return this.conditions.test(lootContext);
    }
}
