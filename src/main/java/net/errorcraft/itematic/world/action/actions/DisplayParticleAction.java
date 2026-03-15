package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.Vec3dProvider;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public record DisplayParticleAction(PositionTarget position, ParticleEffect particle, int count, Vec3dProvider offset, Vec3dProvider delta, double speed, boolean force) implements Action<DisplayParticleAction> {
    public static final MapCodec<DisplayParticleAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(DisplayParticleAction::position),
        ParticleTypes.TYPE_CODEC.fieldOf("particle").forGetter(DisplayParticleAction::particle),
        Codecs.NON_NEGATIVE_INT.fieldOf("count").forGetter(DisplayParticleAction::count),
        Vec3dProvider.CODEC.optionalFieldOf("offset", Vec3dProvider.ZERO).forGetter(DisplayParticleAction::offset),
        Vec3dProvider.CODEC.fieldOf("delta").forGetter(DisplayParticleAction::delta),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("speed").forGetter(DisplayParticleAction::speed),
        Codec.BOOL.optionalFieldOf("force", false).forGetter(DisplayParticleAction::force)
    ).apply(instance, DisplayParticleAction::new));

    public static Builder builder(PositionTarget position, ParticleEffect particle) {
        return new Builder(position, particle);
    }

    @Override
    public ActionType<DisplayParticleAction> type() {
        return ActionTypes.DISPLAY_PARTICLE;
    }

    @Override
    public boolean execute(ActionContext context) {
        return false;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ServerWorld world = context.world();
        Random random = world.getRandom();
        Vec3d pos = this.position(context, random);
        if (pos == null) {
            return false;
        }

        Vec3d delta = this.delta.get(random);
        int amountOfPlayersShown = world.spawnParticles(
            this.particle,
            this.force,
            false,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            this.count,
            delta.getX(),
            delta.getY(),
            delta.getZ(),
            this.speed
        );
        return amountOfPlayersShown > 0;
    }

    private Vec3d position(NewActionContext context, Random random) {
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return null;
        }

        return pos.add(this.offset.get(random));
    }

    public static class Builder {
        private final PositionTarget position;
        private final ParticleEffect particle;
        private int count = 0;
        private Vec3dProvider offset = Vec3dProvider.ZERO;
        private Vec3dProvider delta = Vec3dProvider.ZERO;
        private double speed = 0.0d;
        private boolean force = false;

        private Builder(PositionTarget position, ParticleEffect particle) {
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

        public Builder offset(Vec3dProvider offset) {
            this.offset = offset;
            return this;
        }

        public Builder delta(Vec3dProvider delta) {
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
