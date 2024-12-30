package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.Optional;

public record ActionEntry(Action<?> action, Optional<ActionRequirements> requirements) {
    public static final Codec<ActionEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Action.CODEC.fieldOf("action").forGetter(ActionEntry::action),
        ActionRequirements.CODEC.optionalFieldOf("requirements").forGetter(ActionEntry::requirements)
    ).apply(instance, ActionEntry::new));
    public static final Codec<RegistryEntry<ActionEntry>> REGISTRY_CODEC = RegistryElementCodec.of(ItematicRegistryKeys.ACTION, CODEC);
    public static final Codec<RegistryEntryList<ActionEntry>> REGISTRY_ENTRY_LIST_CODEC = RegistryCodecs.entryList(ItematicRegistryKeys.ACTION, CODEC, true);

    public Optional<Boolean> execute(ActionContext context) {
        if (!this.test(context)) {
            return Optional.empty();
        }
        return Optional.of(this.action.execute(context));
    }

    private boolean test(ActionContext context) {
        return this.requirements.map(requirements -> requirements.test(context))
            .orElse(true);
    }

    public static ActionEntry of(Action<?> action) {
        return new ActionEntry(action, Optional.empty());
    }

    public static ActionEntry of(SequenceHandler.Builder<?, ?> builder) {
        return new ActionEntry(SequenceAction.of(builder), Optional.empty());
    }

    public static ActionEntry of(ActionRequirements requirements, Action<?> action) {
        return new ActionEntry(action, Optional.of(requirements));
    }

    public static ActionEntry of(ActionRequirements requirements, SequenceHandler.Builder<?, ?> builder) {
        return new ActionEntry(SequenceAction.of(builder), Optional.of(requirements));
    }
}
