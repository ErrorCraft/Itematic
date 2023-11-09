package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;

import java.util.List;

public record FirstToPassAction(List<ActionEntry> entries) implements Action {
    public static final Codec<FirstToPassAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionEntry.CODEC.listOf().fieldOf("entries").forGetter(FirstToPassAction::entries)
    ).apply(instance, FirstToPassAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.FIRST_TO_PASS;
    }

    @Override
    public boolean execute(ActionContext context) {
        for (ActionEntry entry : this.entries) {
            if (entry.execute(context)) {
                return true;
            }
        }
        return false;
    }

    public static FirstToPassAction of(ActionEntry... entries) {
        return new FirstToPassAction(List.of(entries));
    }
}
