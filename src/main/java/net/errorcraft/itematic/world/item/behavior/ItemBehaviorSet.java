package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.behaviors.StackableItemBehavior;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemBehaviorSet implements Iterable<ItemBehavior<?>> {
    public static final ItemBehaviorSet EMPTY = new ItemBehaviorSet();
    public static final Codec<ItemBehaviorSet> CODEC = ItemBehavior.SET_CODEC.xmap(ItemBehaviorSet::new, ItemBehaviorSet::values)
        .validate(ItemBehaviorSet::validate);
    private final HashMap<ItemBehaviorType<?>, ItemBehavior<?>> map;

    private ItemBehaviorSet() {
        this(new HashMap<>());
    }

    private ItemBehaviorSet(Set<ItemBehavior<?>> values) {
        this(new HashMap<>(values.stream().collect(Collectors.toMap(ItemBehavior::type, Function.identity()))));
    }

    private ItemBehaviorSet(HashMap<ItemBehaviorType<?>, ItemBehavior<?>> map) {
        this.map = map;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Iterator<ItemBehavior<?>> iterator() {
        return this.map.values().iterator();
    }

    public <T extends ItemBehavior<T>> boolean contains(ItemBehaviorType<T> type) {
        return this.map.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemBehavior<T>> Optional<T> get(ItemBehaviorType<T> type) {
        return Optional.ofNullable((T) this.map.get(type));
    }

    private Set<ItemBehavior<?>> values() {
        return new HashSet<>(this.map.values());
    }

    private static DataResult<ItemBehaviorSet> validate(ItemBehaviorSet set) {
        if (set.contains(ItemBehaviorType.DAMAGEABLE) && set.get(ItemBehaviorType.STACKABLE).map(StackableItemBehavior::maxStackSize).orElse(Items.UNSTACKABLE_MAX_STACK_SIZE) > Items.UNSTACKABLE_MAX_STACK_SIZE) {
            return DataResult.error(() -> "Item cannot be both damageable and stackable");
        }

        return DataResult.success(set);
    }

    public static class Builder {
        private final Set<ItemBehavior<?>> components = new HashSet<>();

        private Builder() {}

        public ItemBehaviorSet build() {
            return new ItemBehaviorSet(this.components);
        }

        public Builder with(ItemBehavior<?> behavior) {
            this.components.add(behavior);
            return this;
        }

        public Builder with(ItemBehavior<?>... behavior) {
            this.components.addAll(List.of(behavior));
            return this;
        }
    }
}
