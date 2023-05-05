package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.world.World;

public record ProjectileItemComponent(RegistryEntry<EntityType<?>> entity) implements ItemComponent {
    public static final Codec<ProjectileItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ENTITY_TYPE).fieldOf("entity").forGetter(ProjectileItemComponent::entity)
    ).apply(instance, ProjectileItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.PROJECTILE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    public Entity createEntity(World world, PlayerEntity user, ItemStack stack, float ySpeedModifierAngle, float speed) {
        Entity entity = this.entity.value().create(world);
        if (entity == null) {
            return null;
        }
        entity.setPosition(user.getEyePos().add(0.0d, -0.1d, 0.0d));
        if (entity instanceof ThrownItemEntity thrownItemEntity) {
            thrownItemEntity.setOwner(user);
            thrownItemEntity.setItem(stack);
            thrownItemEntity.setVelocity(user, user.getPitch(), user.getYaw(), ySpeedModifierAngle, speed, 1.0f);
        }
        world.spawnEntity(entity);
        return entity;
    }
}
