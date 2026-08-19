package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

public record TeleportAction(int distance, LootContext.EntityTarget entity) implements Action<TeleportAction> {
    public static final MapCodec<TeleportAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("distance").forGetter(TeleportAction::distance),
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(TeleportAction::entity)
    ).apply(instance, TeleportAction::new));
    private static final int MAX_TELEPORT_ATTEMPTS = 16;

    public static TeleportAction of(int distance, LootContext.EntityTarget entity) {
        return new TeleportAction(distance, entity);
    }

    @Override
    public ActionType<TeleportAction> type() {
        return ActionType.TELEPORT;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.level() instanceof ServerLevel world)) {
            return false;
        }

        Entity entity = context.get(this.entity.contextParam());
        if (entity instanceof LivingEntity target) {
            return this.teleport(target, world);
        }

        return false;
    }

    private boolean teleport(LivingEntity target, ServerLevel world) {
        Vec3 position = target.position();
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS; i++) {
            double newX = position.x() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            double newY = Math.clamp(
                position.y() + (target.getRandom().nextDouble() - 0.5d) * this.distance,
                world.getMinY(),
                world.getMinY() + world.getLogicalHeight() - 1
            );
            double newZ = position.z() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            if (target.isPassenger()) {
                target.stopRiding();
            }

            if (target.randomTeleport(newX, newY, newZ, true)) {
                teleported(target, world, position);
                return true;
            }
        }

        return false;
    }

    private static void teleported(LivingEntity target, ServerLevel world, Vec3 position) {
        world.gameEvent(GameEvent.TELEPORT, position, GameEvent.Context.of(target));
        SoundEvent soundEvent = soundEvent(target);
        world.itematic$playSound(null, position, soundEvent, target.getSoundSource(), 1.0f, 1.0f);
        target.playSound(soundEvent, 1.0f, 1.0f);
        target.resetFallDistance();
    }

    private static SoundEvent soundEvent(LivingEntity target) {
        if (target instanceof Fox) {
            return SoundEvents.FOX_TELEPORT;
        }

        return SoundEvents.CHORUS_FRUIT_TELEPORT;
    }
}
