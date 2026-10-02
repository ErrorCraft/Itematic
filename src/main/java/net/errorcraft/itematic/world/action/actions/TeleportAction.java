package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record TeleportAction(int distance, LootContext.EntityTarget entity, Optional<HolderSet<Block>> unsafeBlocks, boolean directionalParticles) implements Action<TeleportAction> {
    public static final MapCodec<TeleportAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("distance").forGetter(TeleportAction::distance),
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(TeleportAction::entity),
        RegistryCodecs.holderSet(Registries.BLOCK).optionalFieldOf("unsafe_blocks").forGetter(TeleportAction::unsafeBlocks),
        Codec.BOOL.optionalFieldOf("directional_particles", true).forGetter(TeleportAction::directionalParticles)
    ).apply(instance, TeleportAction::new));
    private static final int MAX_TELEPORT_ATTEMPTS = 16;

    public static TeleportAction of(int distance, LootContext.EntityTarget entity, HolderSet<Block> unsafeBlocks) {
        return new TeleportAction(distance, entity, Optional.of(unsafeBlocks), true);
    }

    @Override
    public ActionType<TeleportAction> type() {
        return ActionType.TELEPORT;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.level() instanceof ServerLevel level)) {
            return false;
        }

        Entity entity = context.get(this.entity.contextParam());
        if (entity instanceof LivingEntity target) {
            return this.teleport(target, level);
        }

        return false;
    }

    private boolean teleport(LivingEntity target, ServerLevel level) {
        Vec3 oldPosition = target.position();
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS; i++) {
            double newX = oldPosition.x() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            double newY = Math.clamp(
                oldPosition.y() + (target.getRandom().nextDouble() - 0.5d) * this.distance,
                level.getMinY(),
                level.getMinY() + level.getLogicalHeight() - 1
            );
            double newZ = oldPosition.z() + (target.getRandom().nextDouble() - 0.5d) * this.distance;
            if (target.isPassenger()) {
                target.stopRiding();
            }

            if (target.randomTeleport(newX, newY, newZ, true, this::isUnsafe)) {
                this.teleported(target, level, oldPosition);
                return true;
            }
        }

        return false;
    }

    private boolean isUnsafe(BlockState state) {
        return this.unsafeBlocks.map(state::is).orElse(false);
    }

    private void teleported(LivingEntity target, ServerLevel level, Vec3 oldPosition) {
        level.gameEvent(GameEvent.TELEPORT, oldPosition, GameEvent.Context.of(target));
        SoundEvent soundEvent = soundEvent(target);
        level.itematic$playSound(null, oldPosition, soundEvent, target.getSoundSource(), 1.0f, 1.0f);
        target.playSound(soundEvent, 1.0f, 1.0f);
        if (this.directionalParticles) {
            BlockPos oldBlockPos = BlockPos.containing(oldPosition);
            BlockPos newBlockPos = target.blockPosition();
            level.levelEvent(
                LevelEvent.PARTICLES_CONSUME_EFFECT_TELEPORT,
                oldBlockPos,
                BlockUtil.clampedPackDifferenceInPosition(oldBlockPos, newBlockPos, 127, 127, 127)
            );
        }

        target.resetFallDistance();
    }

    private static SoundEvent soundEvent(LivingEntity target) {
        if (target instanceof Fox) {
            return SoundEvents.FOX_TELEPORT;
        }

        return SoundEvents.CHORUS_FRUIT_TELEPORT;
    }
}
