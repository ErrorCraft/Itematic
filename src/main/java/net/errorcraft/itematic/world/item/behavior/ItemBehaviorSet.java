package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;

import java.util.HashMap;
import java.util.Map;

public class ItemBehaviorSet {
    public static final ItemBehaviorSet EMPTY = new ItemBehaviorSet(Map.of());
    public static final Codec<ItemBehaviorSet> CODEC = ItemBehavior.MAP_CODEC.xmap(
        ItemBehaviorSet::new,
        itemBehaviorSet -> itemBehaviorSet.behavior
    );
    private final Map<ItemBehaviorType<?>, ItemBehavior<?>> behavior;

    private ItemBehaviorSet(Map<ItemBehaviorType<?>, ItemBehavior<?>> behavior) {
        this.behavior = behavior;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addComponents(DataComponentMap.Builder builder) {
        for (ItemBehavior<?> behavior : this.behavior.values()) {
            behavior.addComponents(builder);
        }
    }

    public static class Builder {
        private final Map<ItemBehaviorType<?>, ItemBehavior<?>> behavior = new HashMap<>();

        private Builder() {}

        public ItemBehaviorSet build() {
            if (this.behavior.isEmpty()) {
                return EMPTY;
            }

            return new ItemBehaviorSet(this.behavior);
        }

        public void add(ItemBehavior<?> behavior) {
            this.behavior.put(behavior.type(), behavior);
        }
    }
}
