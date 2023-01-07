package errorcraft.itematic.item.component;

import com.mojang.serialization.MapCodec;
import errorcraft.itematic.serialisation.SetMapCodec;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemComponentSet implements Iterable<ItemComponent> {
    public static final MapCodec<ItemComponentSet> CODEC = SetMapCodec.ofRegistry(ItemComponentTypes.ITEM_COMPONENT_TYPE, ItemComponentType::codec, ItemComponent::getCodec, ItemComponent::getType).xmap(ItemComponentSet::new, v -> v.values);
    private final Set<ItemComponent> values;
    private final HashMap<ItemComponentType<?>, ItemComponent> map;

    public ItemComponentSet() {
        this(new HashSet<>(), new HashMap<>());
    }

    private ItemComponentSet(Set<ItemComponent> values) {
        this(values, new HashMap<>(values.stream().collect(Collectors.toMap(ItemComponent::getType, Function.identity()))));
    }

    private ItemComponentSet(Set<ItemComponent> values, HashMap<ItemComponentType<?>, ItemComponent> map) {
        this.values = values;
        this.map = map;
    }

    @NotNull
    @Override
    public Iterator<ItemComponent> iterator() {
        return this.values.iterator();
    }

    public <T extends ItemComponent> boolean contains(ItemComponentType<T> type) {
        return this.map.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent> Optional<T> get(ItemComponentType<T> type) {
        return Optional.ofNullable((T) this.map.get(type));
    }

    public static class Builder {
        private final Set<ItemComponent> components = new HashSet<>();

        public static Builder create() {
            return new Builder();
        }

        public Builder with(ItemComponent component) {
            this.components.add(component);
            return this;
        }

        public Builder with(ItemComponent... components) {
            this.components.addAll(Arrays.asList(components));
            return this;
        }

        public ItemComponentSet build() {
            return new ItemComponentSet(this.components);
        }
    }
}
