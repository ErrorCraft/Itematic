package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;

public record ActionRequirements(ActionContextParameters context, LootCondition conditions) {
    public static final Codec<ActionRequirements> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameters.CODEC.fieldOf("context").forGetter(ActionRequirements::context),
        LootConditionTypes.CODEC.fieldOf("conditions").forGetter(ActionRequirements::conditions)
    ).apply(instance, ActionRequirements::new));

    public boolean test(ActionContext context) {
        LootContext lootContext = context.createLootContext(this.context);
        lootContext.markActive(LootContext.predicate(this.conditions));
        return this.conditions.test(lootContext);
    }
}
