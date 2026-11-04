package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.EntityController;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.EntityType;

public record SteeringItemBehavior(EntityController entityController) implements ItemBehavior<SteeringItemBehavior> {
    public static final Codec<SteeringItemBehavior> CODEC = EntityController.CODEC.xmap(
        SteeringItemBehavior::new,
        SteeringItemBehavior::entityController
    );

    public static SteeringItemBehavior of(HolderSet<EntityType<?>> target, int damage) {
        return new SteeringItemBehavior(new EntityController(target, damage));
    }

    @Override
    public ItemBehaviorType<SteeringItemBehavior> type() {
        return ItemBehaviorType.STEERING;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.ENTITY_CONTROLLER, this.entityController);
    }
}
