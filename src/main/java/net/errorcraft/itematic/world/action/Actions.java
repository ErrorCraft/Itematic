package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.world.action.actions.DecrementItemAction;
import net.errorcraft.itematic.world.action.actions.ModifySignAction;
import net.errorcraft.itematic.world.action.actions.SwingHandAction;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.tag.BlockTags;

public class Actions {
    private Actions() {}

    public static ActionEntry waxSign(boolean wax) {
        return modifySign(ModifySignAction.wax(ActionContextParameter.TARGET, wax));
    }

    public static ActionEntry glowSign(boolean glow) {
        return modifySign(ModifySignAction.glow(ActionContextParameter.TARGET, glow));
    }

    private static ActionEntry modifySign(ModifySignAction action) {
        return ActionEntry.passing(
            ActionRequirements.of(
                ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                AllOfLootCondition.builder(
                    LocationCheckLootCondition.builder(
                        LocationPredicate.Builder.create()
                            .block(BlockPredicate.Builder.create()
                                .tag(BlockTags.SIGNS))))
                    .build()
            ),
            action,
            DecrementItemAction.of(1),
            SwingHandAction.INSTANCE
        );
    }
}
