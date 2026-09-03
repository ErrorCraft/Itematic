package net.errorcraft.itematic.world.action;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.minecraft.core.Holder;

public record CancellableActionEntry(Holder<ActionEntry> entry, boolean cancelOriginalCallOnSuccess) {
    private static final Codec<CancellableActionEntry> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionEntry.CODEC.fieldOf("entry").forGetter(CancellableActionEntry::entry),
        Codec.BOOL.fieldOf("cancel_original_call_on_success").forGetter(CancellableActionEntry::cancelOriginalCallOnSuccess)
    ).apply(instance, CancellableActionEntry::new));
    public static final Codec<CancellableActionEntry> CODEC = ItematicCodecs.withAlternative(
        FULL_CODEC,
        ActionEntry.CODEC,
        CancellableActionEntry::new,
        CancellableActionEntry::wrap
    );

    private CancellableActionEntry(Holder<ActionEntry> entry) {
        this(entry, false);
    }

    private static Either<CancellableActionEntry, Holder<ActionEntry>> wrap(CancellableActionEntry cancellableActionEntry) {
        if (cancellableActionEntry.cancelOriginalCallOnSuccess) {
            return Either.left(cancellableActionEntry);
        }

        return Either.right(cancellableActionEntry.entry);
    }
}
