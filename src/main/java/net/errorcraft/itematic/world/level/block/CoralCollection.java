package net.errorcraft.itematic.world.level.block;

import net.errorcraft.itematic.world.item.Items;
import net.minecraft.core.Direction;
import net.minecraft.references.BlockItemId;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record CoralCollection<T>(ByState<T> coral, ByState<T> block, ByState<T> fan, ByState<T> wallFan) {
    public static final CoralCollection<String> PREFIXES = new CoralCollection<>(
        ByState.PREFIXES,
        ByState.PREFIXES,
        ByState.PREFIXES,
        ByState.PREFIXES
    );
    public static final CoralCollection<String> SUFFIXES = new CoralCollection<>(
        ByState.create("_coral"),
        ByState.create("_coral_block"),
        ByState.create("_coral_fan"),
        ByState.create("_coral_wall_fan")
    );

    public static <T> CoralCollection<T> create(T value) {
        return new CoralCollection<>(
            ByState.create(value),
            ByState.create(value),
            ByState.create(value),
            ByState.create(value)
        );
    }

    public static CoralCollection<String> affixWithType(CoralCollection<String> ids) {
        return zipMap(
            zipMap(PREFIXES, ids, (prefix, id) -> prefix + id),
            SUFFIXES,
            (prefixedId, suffix) -> prefixedId + suffix
        );
    }

    public static <T, U, R> CoralCollection<R> zipMap(CoralCollection<T> first, CoralCollection<U> second, BiFunction<T, U, R> operation) {
        return new CoralCollection<>(
            ByState.zipMap(first.coral, second.coral, operation),
            ByState.zipMap(first.block, second.block, operation),
            ByState.zipMap(first.fan, second.fan, operation),
            ByState.zipMap(first.wallFan, second.wallFan, operation)
        );
    }

    public static void registerItems(CoralCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper) {
        blockItems.coral.forEach(bootstrapper::registerBlock);
        blockItems.block.forEach(bootstrapper::registerBlock);
        ByState.zipApply(
            blockItems.fan,
            blockItems.wallFan,
            (fan, wallFan) -> bootstrapper.registerBlockAttachedToSide(
                fan,
                wallFan.block(),
                Direction.DOWN
            )
        );
    }

    public <U> CoralCollection<U> map(Function<T, U> mapper) {
        return new CoralCollection<>(
            this.coral.map(mapper),
            this.block.map(mapper),
            this.fan.map(mapper),
            this.wallFan.map(mapper)
        );
    }

    public record ByState<T>(T alive, T dead) {
        private static final ByState<String> PREFIXES = new ByState<>("", "dead_");

        public static <T> ByState<T> create(T value) {
            return new ByState<>(value, value);
        }

        public static <T, U, R> ByState<R> zipMap(ByState<T> first, ByState<U> second, BiFunction<T, U, R> operation) {
            return new ByState<>(
                operation.apply(first.alive, second.alive),
                operation.apply(first.dead, second.dead)
            );
        }

        public static <T, U> void zipApply(ByState<T> first, ByState<U> second, BiConsumer<T, U> consumer) {
            consumer.accept(first.alive, second.alive);
            consumer.accept(first.dead, second.dead);
        }

        public <U> ByState<U> map(Function<T, U> mapper) {
            return new ByState<>(
                mapper.apply(this.alive),
                mapper.apply(this.dead)
            );
        }

        public void forEach(Consumer<T> consumer) {
            consumer.accept(this.alive);
            consumer.accept(this.dead);
        }
    }
}
