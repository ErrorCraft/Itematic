package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.Vec3dProvider;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record DisplayParticleAction(ActionContextParameter position, ParticleEffect particle, int count, Vec3dProvider offset, Vec3dProvider delta, double speed, boolean force) implements Action<DisplayParticleAction> {
    public static final MapCodec<DisplayParticleAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(DisplayParticleAction::position),
        ParticleTypes.TYPE_CODEC.fieldOf("particle").forGetter(DisplayParticleAction::particle),
        Codec.INT.fieldOf("count").forGetter(DisplayParticleAction::count),
        Vec3dProvider.CODEC.optionalFieldOf("offset", Vec3dProvider.ZERO).forGetter(DisplayParticleAction::offset),
        Vec3dProvider.CODEC.fieldOf("delta").forGetter(DisplayParticleAction::delta),
        Codec.DOUBLE.fieldOf("speed").forGetter(DisplayParticleAction::speed),
        Codec.BOOL.optionalFieldOf("force", false).forGetter(DisplayParticleAction::force)
    ).apply(instance, DisplayParticleAction::new));

    public static Builder builder(ActionContextParameter position, ParticleEffect particle) {
        return new Builder(position, particle);
    }

    @Override
    public ActionType<DisplayParticleAction> type() {
        return ActionTypes.DISPLAY_PARTICLE;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        Vec3d pos = context.position(this.position).add(this.offset.get(world.getRandom()));
        return this.spawnParticles(world, pos);
    }

    private boolean spawnParticles(ServerWorld world, Vec3d pos) {
        Vec3d delta = this.delta.get(world.getRandom());
        int count = 0;
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (world.spawnParticles(player, this.particle, this.force, pos.getX(), pos.getY(), pos.getZ(), this.count, delta.getX(), delta.getY(), delta.getZ(), this.speed)) {
                count++;
            }
        }
        return count > 0;
    }

    public static class Builder {
        private final ActionContextParameter position;
        private final ParticleEffect particle;
        private int count = 0;
        private Vec3dProvider offset = Vec3dProvider.ZERO;
        private Vec3dProvider delta = Vec3dProvider.ZERO;
        private double speed = 0.0d;
        private boolean force = false;

        private Builder(ActionContextParameter position, ParticleEffect particle) {
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
