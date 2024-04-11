package net.errorcraft.itematic.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.item.component.components.MaxStackSizeItemComponent;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemComponentSet implements Iterable<ItemComponent<?>> {
    public static final ItemComponentSet EMPTY = new ItemComponentSet();
    public static final Codec<ItemComponentSet> CODEC = ItemComponent.SET_CODEC.xmap(ItemComponentSet::new, ItemComponentSet::values)
        .validate(ItemComponentSet::validate);
    private final HashMap<ItemComponentType<?>, ItemComponent<?>> map;

    private ItemComponentSet() {
        this(new HashMap<>());
    }

    private ItemComponentSet(Set<ItemComponent<?>> values) {
        this(new HashMap<>(values.stream().collect(Collectors.toMap(ItemComponent::type, Function.identity()))));
    }

    private ItemComponentSet(HashMap<ItemComponentType<?>, ItemComponent<?>> map) {
        this.map = map;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    @Override
    public Iterator<ItemComponent<?>> iterator() {
        return this.map.values().iterator();
    }

    public <T extends ItemComponent<T>> boolean contains(ItemComponentType<T> type) {
        return this.map.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent<T>> Optional<T> get(ItemComponentType<T> type) {
        return Optional.ofNullable((T) this.map.get(type));
    }

    private Set<ItemComponent<?>> values() {
        return new HashSet<>(this.map.values());
    }

    private static DataResult<ItemComponentSet> validate(ItemComponentSet set) {
        if (set.contains(ItemComponentTypes.DAMAGEABLE) && set.get(ItemComponentTypes.MAX_STACK_SIZE).map(MaxStackSizeItemComponent::maxStackSize).orElse(Item.DEFAULT_MAX_COUNT) > 1) {
            return DataResult.error(() -> "Item cannot be both damageable and stackable");
        }
        return DataResult.success(set);
    }

    public static class Builder {
        private final Set<ItemComponent<?>> components = new HashSet<>();

        public Builder with(ItemComponent<?> component) {
            this.components.add(component);
            return this;
        }

        public Builder with(ItemComponent<?>... components) {
            this.components.addAll(Arrays.asList(components));
            return this;
        }

        public ItemComponentSet build() {
            return new ItemComponentSet(this.components);
        }
    }
}
