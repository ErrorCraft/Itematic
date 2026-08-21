package net.errorcraft.itematic.client.resources.item.bar.color.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.advancements.criterion.ItematicMinMaxBounds;
import net.errorcraft.itematic.client.resources.item.bar.color.ColorProvider;
import net.errorcraft.itematic.client.resources.item.bar.color.ColorProviderType;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record FirstToPassConditionColorProvider(List<Entry> entries, ColorProvider fallback) implements ColorProvider {
    public static final MapCodec<FirstToPassConditionColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.nonEmptyList(Entry.CODEC.listOf()).fieldOf("entries").forGetter(FirstToPassConditionColorProvider::entries),
        ColorProvider.CODEC.fieldOf("fallback").forGetter(FirstToPassConditionColorProvider::fallback)
    ).apply(instance, FirstToPassConditionColorProvider::new));

    public static FirstToPassConditionColorProvider of(int fallback, Entry... entries) {
        return new FirstToPassConditionColorProvider(List.of(entries), new ConstantColorProvider(fallback));
    }

    @Override
    public ColorProviderType<?> type() {
        return ColorProviderType.FIRST_TO_PASS_CONDITION;
    }

    @Override
    public int get(float progress) {
        for (Entry entry : this.entries) {
            if (entry.condition.test(progress)) {
                return entry.color.get(progress);
            }
        }

        return this.fallback.get(progress);
    }

    public record Entry(ColorProvider color, Condition condition) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ColorProvider.CODEC.fieldOf("color").forGetter(Entry::color),
            Condition.CODEC.fieldOf("condition").forGetter(Entry::condition)
        ).apply(instance, Entry::new));

        public static Entry of(int color, float progress) {
            return new Entry(
                new ConstantColorProvider(color),
                new Condition(ItematicMinMaxBounds.Floats.exactly(progress))
            );
        }
    }

    public record Condition(ItematicMinMaxBounds.Floats progress) {
        public static final Codec<Condition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItematicMinMaxBounds.Floats.CODEC.fieldOf("progress").forGetter(Condition::progress)
        ).apply(instance, Condition::new));

        public boolean test(float progress) {
            return this.progress.test(progress);
        }
    }
}
