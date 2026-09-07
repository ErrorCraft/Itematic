package net.errorcraft.itematic.resources;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryValidator;
import net.minecraft.resources.ResourceKey;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.SequencedSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItematicRegistryValidators {
    private ItematicRegistryValidators() {}

    public static <T> RegistryValidator<T> nonRecursive(Function<T, Stream<Holder.Reference<T>>> subElementSupplier) {
        return new NonRecursiveRegistryValidator<>(subElementSupplier);
    }

    private record NonRecursiveRegistryValidator<T>(Function<T, Stream<Holder.Reference<T>>> subElementSupplier) implements RegistryValidator<T> {
        @Override
        public void validate(Registry<T> registry, Map<ResourceKey<?>, Exception> loadingErrors) {
            registry.listElements().forEach(element -> {
                try {
                    this.validate(new LinkedHashSet<>(), element);
                } catch (IllegalStateException e) {
                    loadingErrors.put(element.key(), e);
                }
            });
        }

        private void validate(SequencedSet<Holder.Reference<T>> found, Holder.Reference<T> element) {
            if (!found.add(element)) {
                throw new IllegalStateException("Recursive element found: " + sequence(found, element));
            }

            this.subElementSupplier.apply(element.value())
                .forEach(subElement -> this.validate(found, subElement));
            found.remove(element);
        }

        private static <T> String sequence(SequencedSet<Holder.Reference<T>> found, Holder.Reference<T> towards) {
            return Stream.concat(found.stream(), Stream.of(towards))
                .map(Holder.Reference::key)
                .map(ResourceKey::identifier)
                .map(Identifier::toString)
                .collect(Collectors.joining(" -> "));
        }
    }
}
