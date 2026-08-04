package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public class SpawnEggItemComponent implements ItemComponent<SpawnEggItemComponent> {
    public static final SpawnEggItemComponent INSTANCE = new SpawnEggItemComponent();
    public static final Codec<SpawnEggItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);

    private SpawnEggItemComponent() {}

    @Override
    public ItemComponentType<SpawnEggItemComponent> type() {
        return ItemComponentTypes.SPAWN_EGG;
    }

    @Override
    public Codec<SpawnEggItemComponent> codec() {
        return CODEC;
    }

    public Optional<Mob> spawnBaby(Player user, Mob entity, EntityType<? extends Mob> entityType, ServerLevel world, Vec3 pos, ItemStack stack) {
        Optional<EntityItemComponent> entityBehavior = stack.itematic$getBehavior(ItemComponentTypes.ENTITY);
        if (entityBehavior.isEmpty()) {
            return Optional.empty();
        }

        if (entityBehavior.get().entity().entityType(stack) != entityType) {
            return Optional.empty();
        }

        Mob child = this.createEntity(entity, entityType, world);
        if (child == null) {
            return Optional.empty();
        }

        if (!child.itematic$trySetBaby(true)) {
            return Optional.empty();
        }

        child.snapTo(pos);
        Component customName = stack.get(DataComponents.CUSTOM_NAME);
        if (customName != null) {
            child.setCustomName(customName);
        }

        world.addFreshEntityWithPassengers(child);
        stack.consume(1, user);
        return Optional.of(child);
    }

    private Mob createEntity(Mob entity, EntityType<? extends Mob> entityType, ServerLevel world) {
        if (entity instanceof AgeableMob passiveEntity) {
            return passiveEntity.getBreedOffspring(world, passiveEntity);
        }

        return entityType.create(world, EntitySpawnReason.SPAWN_ITEM_USE);
    }
}
