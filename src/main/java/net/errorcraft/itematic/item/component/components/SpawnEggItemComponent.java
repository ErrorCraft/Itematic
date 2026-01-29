package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.item.color.colors.IndexItemColor;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public record SpawnEggItemComponent() implements ItemComponent<SpawnEggItemComponent> {
    public static final SpawnEggItemComponent INSTANCE = new SpawnEggItemComponent();
    public static final Codec<SpawnEggItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<SpawnEggItemComponent> type() {
        return ItemComponentTypes.SPAWN_EGG;
    }

    @Override
    public Codec<SpawnEggItemComponent> codec() {
        return CODEC;
    }

    public Optional<MobEntity> spawnBaby(PlayerEntity user, MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world, Vec3d pos, ItemStack stack) {
        Optional<EntityItemComponent> entityItemComponent = stack.itematic$getComponent(ItemComponentTypes.ENTITY);
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
        if (!mobEntity.itematic$trySetBaby(true)) {
            return Optional.empty();
        }
        mobEntity.refreshPositionAfterTeleport(pos);
        Text customName = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) {
            mobEntity.setCustomName(customName);
        }
        world.spawnEntityAndPassengers(mobEntity);
        stack.decrementUnlessCreative(1, user);
        return Optional.of(mobEntity);
    }

    public static ItemComponent<?>[] from(RegistryEntry<EntityType<?>> entity, int primaryColor, int secondaryColor, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            EntityItemComponent.of(SimpleEntityInitializer.of(entity.value()), true, EntityItemComponent.Pass.BLOCK, EntityItemComponent.Pass.FLUID),
            INSTANCE,
            TintedItemComponent.of(IndexItemColor.of(primaryColor, secondaryColor)),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM))
        };
    }

    private MobEntity createEntity(MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world) {
        if (entity instanceof PassiveEntity passiveEntity) {
            return passiveEntity.createChild(world, passiveEntity);
        }

        return entityType.create(world, SpawnReason.SPAWN_EGG);
    }
}
