package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public record TeleportAction(int distance, LootContext.EntityTarget entity) implements Action<TeleportAction> {
    public static final MapCodec<TeleportAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("distance").forGetter(TeleportAction::distance),
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(TeleportAction::entity)
    ).apply(instance, TeleportAction::new));
    private static final int MAX_TELEPORT_ATTEMPTS = 16;

    public static TeleportAction of(int distance, LootContext.EntityTarget entity) {
        return new TeleportAction(distance, entity);
    }

    @Override
    public ActionType<TeleportAction> type() {
        return ActionTypes.TELEPORT;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.world() instanceof ServerWorld world)) {
            return false;
        }

        Entity entity = context.get(this.entity.getParameter());
        if (entity instanceof LivingEntity target) {
            return this.teleport(target, world);
        }

        return false;
    }

    private boolean teleport(LivingEntity target, ServerWorld world) {
        Vec3d position = target.getPos();
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS; i++) {
            double newX = position.getX() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            double newY = Math.clamp(
                position.getY() + (target.getRandom().nextDouble() - 0.5d) * this.distance,
                world.getBottomY(),
                world.getBottomY() + world.getLogicalHeight() - 1
            );
            double newZ = position.getZ() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            if (target.hasVehicle()) {
                target.stopRiding();
            }

            if (target.teleport(newX, newY, newZ, true)) {
                teleported(target, world, position);
                return true;
            }
        }

        return false;
    }

    private static void teleported(LivingEntity target, ServerWorld world, Vec3d position) {
        world.emitGameEvent(GameEvent.TELEPORT, position, GameEvent.Emitter.of(target));
        SoundEvent soundEvent = soundEvent(target);
        world.itematic$playSound(null, position, soundEvent, target.getSoundCategory(), 1.0f, 1.0f);
        target.playSound(soundEvent, 1.0f, 1.0f);
        target.onLanding();
    }

    private static SoundEvent soundEvent(LivingEntity target) {
        if (target instanceof FoxEntity) {
            return SoundEvents.ENTITY_FOX_TELEPORT;
        }

        return SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
    }
}
