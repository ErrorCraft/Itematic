package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public record TeleportAction(int distance) implements Action {
    public static final Codec<TeleportAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("distance").forGetter(TeleportAction::distance)
    ).apply(instance, TeleportAction::new));
    private static final int MAX_TELEPORT_ATTEMPTS = 16;

    @Override
    public ActionType<?> type() {
        return ActionTypes.TELEPORT;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (context.target().isEmpty()) {
            return false;
        }
        if (context.target().get() instanceof LivingEntity target) {
            return this.teleport(target, context.world());
        }
        return false;
    }

    private boolean teleport(LivingEntity target, ServerWorld world) {
        Vec3d position = target.getPos();
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS; i++) {
            double newX = position.getX() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            double newY = MathHelper.clamp(position.getY() + target.getRandom().nextInt(this.distance) - (this.distance * 0.5d), world.getBottomY(), world.getBottomY() + world.getLogicalHeight() - 1);
            double newZ = position.getZ() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            if (target.hasVehicle()) {
                target.stopRiding();
            }
            if (target.teleport(newX, newY, newZ, true)) {
                this.teleported(target, world, position);
                return true;
            }
        }
        return false;
    }

    private void teleported(LivingEntity target, ServerWorld world, Vec3d position) {
        world.emitGameEvent(GameEvent.TELEPORT, position, GameEvent.Emitter.of(target));
        SoundEvent soundEvent = getSoundEvent(target);
        world.playSound(null, position, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
        target.playSound(soundEvent, 1.0f, 1.0f);
    }

    private static SoundEvent getSoundEvent(LivingEntity target) {
        if (target instanceof FoxEntity) {
            return SoundEvents.ENTITY_FOX_TELEPORT;
        }
        return SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
    }
}
