package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.world.action.ActionEntry;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecursionValidator {
    private final Set<Holder.Reference<ActionEntry>> foundEntries = new LinkedHashSet<>();

    public RecursionValidator(Holder.Reference<ActionEntry> initialEntry) {
        this.foundEntries.add(initialEntry);
    }

    public void add(Holder.Reference<ActionEntry> entry) {
        if (!this.foundEntries.add(entry)) {
            throw new IllegalStateException("Recursive action found: " + this.sequence(entry));
        }
    }

    public void remove(Holder.Reference<ActionEntry> entry) {
        if (!this.foundEntries.remove(entry)) {
            throw new IllegalStateException("Action " + entry.key().identifier() + " is not present in sequence: " + this.sequence(entry));
        }
    }

    private String sequence(Holder.Reference<ActionEntry> towardsEntry) {
        return Stream.concat(this.foundEntries.stream(), Stream.of(towardsEntry))
            .map(Holder.Reference::key)
            .map(ResourceKey::identifier)
            .map(Identifier::toString)
            .collect(Collectors.joining(" -> "));
    }
}
