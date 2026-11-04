package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.ProjectileComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.entity.EntityType;

public record ProjectileItemBehavior(ProjectileComponent projectile) implements ItemBehavior<ProjectileItemBehavior> {
    public static final Codec<ProjectileItemBehavior> CODEC = ProjectileComponent.CODEC.xmap(
        ProjectileItemBehavior::new,
        ProjectileItemBehavior::projectile
    );

    public static ProjectileItemBehavior of(Holder<EntityType<?>> entity) {
        return new ProjectileItemBehavior(
            new ProjectileComponent(
                EntitySpawner.of(entity)
            )
        );
    }

    public static ProjectileItemBehavior of(Holder<EntityType<?>> entity, DataComponentPatch components) {
        return new ProjectileItemBehavior(
            new ProjectileComponent(
                EntitySpawner.builder(entity)
                    .components(components)
                    .build()
            )
        );
    }

    @Override
    public ItemBehaviorType<ProjectileItemBehavior> type() {
        return ItemBehaviorType.PROJECTILE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.PROJECTILE, this.projectile);
    }
}
