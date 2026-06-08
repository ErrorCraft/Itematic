package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class SpawnEggItemComponent implements ItemComponent<SpawnEggItemComponent> {
    public static final SpawnEggItemComponent INSTANCE = new SpawnEggItemComponent();
    public static final Codec<SpawnEggItemComponent> CODEC = Codec.unit(INSTANCE);

    private SpawnEggItemComponent() {}

    @Override
    public ItemComponentType<SpawnEggItemComponent> type() {
        return ItemComponentTypes.SPAWN_EGG;
    }

    @Override
    public Codec<SpawnEggItemComponent> codec() {
        return CODEC;
    }

    public Optional<MobEntity> spawnBaby(PlayerEntity user, MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world, Vec3d pos, ItemStack stack) {
        Optional<EntityItemComponent> entityBehavior = stack.itematic$getBehavior(ItemComponentTypes.ENTITY);
        if (entityBehavior.isEmpty()) {
            return Optional.empty();
        }

        if (entityBehavior.get().entity().entityType(stack, world.getRegistryManager()) != entityType) {
            return Optional.empty();
        }

        MobEntity child = this.createEntity(entity, entityType, world);
        if (child == null) {
            return Optional.empty();
        }

        if (!child.itematic$trySetBaby(true)) {
            return Optional.empty();
        }

        child.refreshPositionAfterTeleport(pos);
        Text customName = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) {
            child.setCustomName(customName);
        }

        world.spawnEntityAndPassengers(child);
        stack.decrementUnlessCreative(1, user);
        return Optional.of(child);
    }

    private MobEntity createEntity(MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world) {
        if (entity instanceof PassiveEntity passiveEntity) {
            return passiveEntity.createChild(world, passiveEntity);
        }

        return entityType.create(world, SpawnReason.SPAWN_ITEM_USE);
    }
}
