package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;

public record RandomizeSequenceHandler(RegistryEntryList<ActionEntry> entries, Optional<Integer> count) implements SequenceHandler<RandomizeSequenceHandler> {
    public static final MapCodec<RandomizeSequenceHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionEntry.REGISTRY_ENTRY_LIST_CODEC.fieldOf("entries").forGetter(RandomizeSequenceHandler::entries),
        Codecs.POSITIVE_INT.optionalFieldOf("count").forGetter(RandomizeSequenceHandler::count)
    ).apply(instance, RandomizeSequenceHandler::new));

    @Override
    public SequenceHandlerType<RandomizeSequenceHandler> type() {
        return SequenceHandlerTypes.RANDOMIZE;
    }

    @Override
    public boolean handle(NewActionContext context) {
        boolean result = false;
        for (RegistryEntry<ActionEntry> entry : this.randomEntries(context.world().getRandom())) {
            result |= entry.value().execute(context).orElse(false);
        }

        return result;
    }

    @Override
    public Iterable<RegistryEntry<ActionEntry>> iterateEntries() {
        return this.entries;
    }

    private Iterable<RegistryEntry<ActionEntry>> randomEntries(Random random) {
        return this.count.map(count -> this.entries.itematic$getRandom(random, count))
            .orElseGet(() -> {
                List<RegistryEntry<ActionEntry>> entries = this.entries.stream().toList();
                Util.shuffle(entries, random);
                return entries;
            });
    }
}
