package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import java.util.Map;

public class ActionValidator {
    private final Registry<ActionEntry> registry;

    public ActionValidator(Registry<ActionEntry> registry) {
        this.registry = registry;
    }

    public void validate(Map<ResourceKey<?>, Exception> exceptions) {
        this.registry.listElements().forEach(entry -> {
            RecursionValidator validator = new RecursionValidator(entry);
            try {
                this.validate(validator, entry);
            } catch (Exception e) {
                exceptions.put(entry.key(), e);
            }
        });
    }

    private void validate(RecursionValidator validator, Holder.Reference<ActionEntry> entry) {
        if (entry.value().action() instanceof SequenceAction action) {
            action.validate(validator);
        }
    }
}
