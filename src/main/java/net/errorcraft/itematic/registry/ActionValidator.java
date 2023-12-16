package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;

public class ActionValidator {
    private final Registry<ActionEntry> registry;

    public ActionValidator(Registry<ActionEntry> registry) {
        this.registry = registry;
    }

    public void validate(Map<RegistryKey<?>, Exception> exceptions) {
        this.registry.streamEntries().forEach(entry -> {
            RecursionValidator validator = new RecursionValidator(entry);
            try {
                this.validate(validator, entry);
            } catch (Exception e) {
                exceptions.put(entry.registryKey(), e);
            }
        });
    }

    private void validate(RecursionValidator validator, RegistryEntry.Reference<ActionEntry> entry) {
        if (entry.value().action() instanceof SequenceAction action) {
            action.validate(validator);
        }
    }
}
