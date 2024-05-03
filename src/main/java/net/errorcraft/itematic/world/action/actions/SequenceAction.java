package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.registry.RecursionValidator;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.minecraft.registry.entry.RegistryEntry;

public record SequenceAction(SequenceHandler<?> handler) implements Action<SequenceAction> {
    public static final MapCodec<SequenceAction> CODEC = SequenceHandler.CODEC.xmap(SequenceAction::new, SequenceAction::handler);

    public static SequenceAction of(SequenceHandler.Builder<?, ?> builder) {
        return new SequenceAction(builder.build());
    }

    public static SequenceAction of(SequenceHandler<?> handler) {
        return new SequenceAction(handler);
    }

    @Override
    public ActionType<SequenceAction> type() {
        return ActionTypes.SEQUENCE;
    }

    @Override
    public boolean execute(ActionContext context) {
        return this.handler.handle(context);
    }

    public void validate(RecursionValidator validator) {
        for (RegistryEntry<ActionEntry> entry : this.handler.iterateEntries()) {
            validateEntry(validator, entry);
        }
    }

    private static void validateEntry(RecursionValidator validator, RegistryEntry<ActionEntry> entry) {
        if (!(entry instanceof RegistryEntry.Reference<ActionEntry> referenceEntry)) {
            return;
        }
        validator.add(referenceEntry);
        if (referenceEntry.value().action() instanceof SequenceAction action) {
            action.validate(validator);
        }
    }
}
