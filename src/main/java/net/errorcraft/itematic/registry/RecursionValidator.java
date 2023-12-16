package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.world.action.ActionEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecursionValidator {
    private final Set<RegistryEntry.Reference<ActionEntry>> foundEntries = new LinkedHashSet<>();

    public RecursionValidator(RegistryEntry.Reference<ActionEntry> initialEntry) {
        this.foundEntries.add(initialEntry);
    }

    public void add(RegistryEntry.Reference<ActionEntry> entry) {
        if (!this.foundEntries.add(entry)) {
            throw new IllegalStateException("Recursive action found: " + this.sequence(entry));
        }
    }

    private String sequence(RegistryEntry.Reference<ActionEntry> towardsEntry) {
        return Stream.concat(this.foundEntries.stream(), Stream.of(towardsEntry))
            .map(RegistryEntry.Reference::registryKey)
            .map(RegistryKey::getValue)
            .map(Identifier::toString)
            .collect(Collectors.joining(" -> "));
    }
}
