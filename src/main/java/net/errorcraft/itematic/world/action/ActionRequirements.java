package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;

public record ActionRequirements(LootCondition conditions) {
    public static final Codec<ActionRequirements> CODEC = LootCondition.CODEC.xmap(ActionRequirements::new, ActionRequirements::conditions);

    public static ActionRequirements of(LootCondition conditions) {
        return new ActionRequirements(conditions);
    }

    public boolean test(NewActionContext context) {
        LootContext lootContext = context.lootContext();
        lootContext.markActive(LootContext.predicate(this.conditions));
        return this.conditions.test(lootContext);
    }
}
