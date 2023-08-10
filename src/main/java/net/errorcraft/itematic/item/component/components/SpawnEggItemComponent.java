package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.item.color.colors.IndexItemColor;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviorKeys;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public record SpawnEggItemComponent() implements ItemComponent {
    public static final Codec<SpawnEggItemComponent> CODEC = Codec.unit(new SpawnEggItemComponent());

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.SPAWN_EGG;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    public Optional<MobEntity> spawnBaby(PlayerEntity user, MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world, Vec3d pos, ItemStack stack) {
        Optional<EntityItemComponent> entityItemComponent = stack.getComponent(ItemComponentTypes.ENTITY);
        if (entityItemComponent.isEmpty()) {
            return Optional.empty();
        }
        if (entityItemComponent.get().getEntityInitializer(stack).type() != entityType) {
            return Optional.empty();
        }
        MobEntity mobEntity = this.createEntity(entity, entityType, world);
        if (mobEntity == null) {
            return Optional.empty();
        }
        if (!mobEntity.trySetBaby(true)) {
            return Optional.empty();
        }
        mobEntity.refreshPositionAfterTeleport(pos);
        if (stack.hasCustomName()) {
            mobEntity.setCustomName(stack.getName());
        }
        world.spawnEntityAndPassengers(mobEntity);
        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        return Optional.of(mobEntity);
    }

    public static ItemComponent[] from(RegistryEntry<EntityType<?>> entity, int primaryColor, int secondaryColor, RegistryEntryLookup<DispenserBehavior> dispenseBehaviors) {
        return new ItemComponent[] {
            new EntityItemComponent(new SimpleEntityInitializer<>(entity.value()), true),
            new SpawnEggItemComponent(),
            new CanPlaceOnFluidsItemComponent(RaycastContext.FluidHandling.SOURCE_ONLY, true),
            new TintedItemComponent(IndexItemColor.of(primaryColor, secondaryColor)),
            new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ENTITY))
        };
    }

    private MobEntity createEntity(MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world) {
        if (entity instanceof PassiveEntity passiveEntity) {
            return passiveEntity.createChild(world, passiveEntity);
        }
        return entityType.create(world);
    }
}
