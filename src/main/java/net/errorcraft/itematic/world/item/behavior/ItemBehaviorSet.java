package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.behaviors.StackableItemBehavior;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ItemBehaviorSet implements Iterable<ItemBehavior<?>> {
    public static final ItemBehaviorSet EMPTY = new ItemBehaviorSet(Map.of());
    public static final Codec<ItemBehaviorSet> CODEC = ItemBehavior.MAP_CODEC.xmap(ItemBehaviorSet::new, itemBehavior -> itemBehavior.behavior)
        .validate(ItemBehaviorSet::validate);
    private final Map<ItemBehaviorType<?>, ItemBehavior<?>> behavior;

    private ItemBehaviorSet(Map<ItemBehaviorType<?>, ItemBehavior<?>> behavior) {
        this.behavior = behavior;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Iterator<ItemBehavior<?>> iterator() {
        return this.behavior.values().iterator();
    }

    public <T extends ItemBehavior<T>> boolean has(ItemBehaviorType<T> type) {
        return this.behavior.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemBehavior<T>> Optional<T> get(ItemBehaviorType<T> type) {
        return Optional.ofNullable((T) this.behavior.get(type));
    }

    private static DataResult<ItemBehaviorSet> validate(ItemBehaviorSet behavior) {
        if (behavior.has(ItemBehaviorType.DAMAGEABLE) && behavior.get(ItemBehaviorType.STACKABLE).map(StackableItemBehavior::maxStackSize).orElse(Items.UNSTACKABLE_MAX_STACK_SIZE) > Items.UNSTACKABLE_MAX_STACK_SIZE) {
            return DataResult.error(() -> "Item cannot be both damageable and stackable");
        }

        return DataResult.success(behavior);
    }

    public static class Builder {
        private final Map<ItemBehaviorType<?>, ItemBehavior<?>> behavior = new HashMap<>();

        private Builder() {}

        public ItemBehaviorSet build() {
            return new ItemBehaviorSet(this.behavior);
        }

        public Builder with(ItemBehavior<?> behavior) {
            this.behavior.put(behavior.type(), behavior);
            return this;
        }

        public Builder with(ItemBehavior<?>... behavior) {
            Stream.of(behavior).forEach(this::with);
            return this;
        }
    }
}
