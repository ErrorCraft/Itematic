package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
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

public class SpawnEggItemBehavior implements ItemBehavior<SpawnEggItemBehavior> {
    public static final SpawnEggItemBehavior INSTANCE = new SpawnEggItemBehavior();
    public static final Codec<SpawnEggItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private SpawnEggItemBehavior() {}

    @Override
    public ItemBehaviorType<SpawnEggItemBehavior> type() {
        return ItemBehaviorType.SPAWN_EGG;
    }

    public Optional<Mob> spawnBaby(Player user, Mob entity, EntityType<? extends Mob> entityType, ServerLevel level, Vec3 pos, ItemStack stack) {
        Optional<EntityItemBehavior> entityBehavior = stack.itematic$getBehavior(ItemBehaviorType.ENTITY);
        if (entityBehavior.isEmpty()) {
            return Optional.empty();
        }

        if (entityBehavior.get().entity().entityType(stack) != entityType) {
            return Optional.empty();
        }

        Mob child = this.createEntity(entity, entityType, level);
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

        level.addFreshEntityWithPassengers(child);
        stack.consume(1, user);
        return Optional.of(child);
    }

    private Mob createEntity(Mob entity, EntityType<? extends Mob> entityType, ServerLevel level) {
        if (entity instanceof AgeableMob passiveEntity) {
            return passiveEntity.getBreedOffspring(level, passiveEntity);
        }

        return entityType.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
    }
}
