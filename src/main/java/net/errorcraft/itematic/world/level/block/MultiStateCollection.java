package net.errorcraft.itematic.world.level.block;

import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record MultiStateCollection<T>(Set<T> elements) {
    private static final MultiStateCollection<?> EMPTY = new MultiStateCollection<>(Set.of());

    @SuppressWarnings("unchecked")
    public static <T> MultiStateCollection<T> empty() {
        return (MultiStateCollection<T>) EMPTY;
    }

    public static <T> MultiStateCollection<T> create(@Nullable T element) {
        if (element == null) {
            return empty();
        }

        return new MultiStateCollection<T>(Set.of(element));
    }

    public static <T> MultiStateCollection<T> create(Set<T> elements) {
        return new MultiStateCollection<>(elements);
    }

    public static <T, U, R> MultiStateCollection<R> zipMap(MultiStateCollection<T> first, MultiStateCollection<U> second, BiFunction<T, U, R> operation) {
        if (first.elements.isEmpty() || second.elements.isEmpty()) {
            return empty();
        }

        Set<R> elements = HashSet.newHashSet(first.elements.size() * second.elements.size());
        for (T firstElement : first.elements) {
            for (U secondElement : second.elements) {
                elements.add(operation.apply(firstElement, secondElement));
            }
        }

        return new MultiStateCollection<>(elements);
    }

    public <U> MultiStateCollection<U> map(Function<T, U> mapper) {
        if (this.elements.isEmpty()) {
            return empty();
        }

        Set<U> elements = HashSet.newHashSet(this.elements.size());
        for (T element : this.elements) {
            elements.add(mapper.apply(element));
        }

        return new MultiStateCollection<>(elements);
    }

    public void forEach(Consumer<T> consumer) {
        this.elements.forEach(consumer);
    }
}
