package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import java.util.List;
import java.util.Optional;

public record RandomizeSequenceHandler(HolderSet<ActionEntry> entries, Optional<Integer> count) implements SequenceHandler<RandomizeSequenceHandler> {
    public static final MapCodec<RandomizeSequenceHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionEntry.LIST_CODEC.fieldOf("entries").forGetter(RandomizeSequenceHandler::entries),
        ExtraCodecs.POSITIVE_INT.optionalFieldOf("count").forGetter(RandomizeSequenceHandler::count)
    ).apply(instance, RandomizeSequenceHandler::new));

    @Override
    public SequenceHandlerType<RandomizeSequenceHandler> type() {
        return SequenceHandlerType.RANDOMIZE;
    }

    @Override
    public boolean handle(ActionContext context) {
        boolean result = false;
        for (Holder<ActionEntry> entry : this.randomEntries(context.level().getRandom())) {
            result |= entry.value().execute(context).orElse(false);
        }

        return result;
    }

    private Iterable<Holder<ActionEntry>> randomEntries(RandomSource random) {
        return this.count.map(count -> this.entries.itematic$getRandom(random, count))
            .orElseGet(() -> {
                List<Holder<ActionEntry>> entries = this.entries.stream().toList();
                Util.shuffle(entries, random);
                return entries;
            });
    }
}
