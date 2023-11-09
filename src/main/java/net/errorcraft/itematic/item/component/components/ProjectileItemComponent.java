package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.world.World;

public record ProjectileItemComponent(RegistryEntry<EntityType<?>> entity, int damage, float chargedSpeed) implements ItemComponent {
    public static final Codec<ProjectileItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ENTITY_TYPE).fieldOf("entity").forGetter(ProjectileItemComponent::entity),
        Codec.INT.optionalFieldOf("damage", 0).forGetter(ProjectileItemComponent::damage),
        Codec.FLOAT.optionalFieldOf("charged_speed", CrossbowItemAccessor.getDefaultSpeed()).forGetter(ProjectileItemComponent::chargedSpeed)
    ).apply(instance, ProjectileItemComponent::new));

    public ProjectileItemComponent(RegistryEntry<EntityType<?>> entity) {
        this(entity, 0);
    }

    public ProjectileItemComponent(RegistryEntry<EntityType<?>> entity, int damage) {
        this(entity, damage, CrossbowItemAccessor.getDefaultSpeed());
    }

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.PROJECTILE;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    public Entity createEntity(World world, LivingEntity user, ItemStack stack, float angleOffset, float speed) {
        Entity entity = this.entity.value().create(world);
        if (entity == null) {
            return null;
        }
        entity.setPosition(user.getEyePos().add(0.0d, -0.1d, 0.0d));
        if (entity instanceof ThrownItemEntity thrownItemEntity) {
            thrownItemEntity.setItem(stack);
        }
        if (entity instanceof ProjectileEntity projectileEntity) {
            projectileEntity.setOwner(user);
            projectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), angleOffset, speed, 1.0f);
        }
        return entity;
    }
}
