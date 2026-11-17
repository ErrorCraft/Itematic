package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.component.ConsumableComponent;
import net.minecraft.core.component.DataComponentMap;

public record ConsumableItemBehavior(ConsumableComponent consumable) implements ItemBehavior {
    public static final Codec<ConsumableItemBehavior> CODEC = ConsumableComponent.CODEC.xmap(
        ConsumableItemBehavior::new,
        ConsumableItemBehavior::consumable
    );

    public static ConsumableItemBehavior of() {
        return new ConsumableItemBehavior(new ConsumableComponent(1));
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.CONSUMABLE, this.consumable);
    }
}
