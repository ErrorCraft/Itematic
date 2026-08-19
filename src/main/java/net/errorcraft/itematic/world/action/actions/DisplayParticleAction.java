package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.phys.Vec3Provider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public record DisplayParticleAction(PositionTarget position, ParticleOptions particle, int count, Vec3Provider offset, Vec3Provider delta, double speed, boolean force) implements Action<DisplayParticleAction> {
    public static final MapCodec<DisplayParticleAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(DisplayParticleAction::position),
        ParticleTypes.CODEC.fieldOf("particle").forGetter(DisplayParticleAction::particle),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("count").forGetter(DisplayParticleAction::count),
        Vec3Provider.CODEC.optionalFieldOf("offset", Vec3Provider.ZERO).forGetter(DisplayParticleAction::offset),
        Vec3Provider.CODEC.fieldOf("delta").forGetter(DisplayParticleAction::delta),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("speed").forGetter(DisplayParticleAction::speed),
        Codec.BOOL.optionalFieldOf("force", false).forGetter(DisplayParticleAction::force)
    ).apply(instance, DisplayParticleAction::new));

    public static Builder builder(PositionTarget position, ParticleOptions particle) {
        return new Builder(position, particle);
    }

    @Override
    public ActionType<DisplayParticleAction> type() {
        return ActionTypes.DISPLAY_PARTICLE;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.world() instanceof ServerLevel world)) {
            return false;
        }

        RandomSource random = world.getRandom();
        Vec3 pos = this.position(context, random);
        if (pos == null) {
            return false;
        }

        Vec3 delta = this.delta.get(random);
        int amountOfPlayersShown = world.sendParticles(
            this.particle,
            this.force,
            false,
            pos.x(),
            pos.y(),
            pos.z(),
            this.count,
            delta.x(),
            delta.y(),
            delta.z(),
            this.speed
        );
        return amountOfPlayersShown > 0;
    }

    private Vec3 position(ActionContext context, RandomSource random) {
        Vec3 pos = context.get(this.position.contextParam());
        if (pos == null) {
            return null;
        }

        return pos.add(this.offset.get(random));
    }

    public static class Builder {
        private final PositionTarget position;
        private final ParticleOptions particle;
        private int count = 0;
        private Vec3Provider offset = Vec3Provider.ZERO;
        private Vec3Provider delta = Vec3Provider.ZERO;
        private double speed = 0.0d;
        private boolean force = false;

        private Builder(PositionTarget position, ParticleOptions particle) {
            this.position = position;
            this.particle = particle;
        }

        public DisplayParticleAction build() {
            return new DisplayParticleAction(this.position, this.particle, this.count, this.offset, this.delta, this.speed, this.force);
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder offset(Vec3Provider offset) {
            this.offset = offset;
            return this;
        }

        public Builder delta(Vec3Provider delta) {
            this.delta = delta;
            return this;
        }

        public Builder speed(double speed) {
            this.speed = speed;
            return this;
        }

        public Builder force() {
            this.force = true;
            return this;
        }
    }
}
