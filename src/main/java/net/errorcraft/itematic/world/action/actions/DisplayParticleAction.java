package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;

public record DisplayParticleAction(ActionContextParameter position, ParticleEffect particle, int count, Vec3d offset, Vec3d delta, double speed) implements Action {
    public static final Codec<DisplayParticleAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(DisplayParticleAction::position),
        ParticleTypes.TYPE_CODEC.fieldOf("particle").forGetter(DisplayParticleAction::particle),
        Codec.INT.fieldOf("count").forGetter(DisplayParticleAction::count),
        Codecs.createStrictOptionalFieldCodec(Vec3d.CODEC, "offset", Vec3d.ZERO).forGetter(DisplayParticleAction::offset),
        Vec3d.CODEC.fieldOf("delta").forGetter(DisplayParticleAction::delta),
        Codec.DOUBLE.fieldOf("speed").forGetter(DisplayParticleAction::speed)
    ).apply(instance, DisplayParticleAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.DISPLAY_PARTICLE;
    }

    @Override
    public boolean execute(ActionContext context) {
        Vec3d pos = context.position(this.position).add(this.offset);
        return context.world().spawnParticles(this.particle, pos.getX(), pos.getY(), pos.getZ(), this.count, this.delta.getX(), this.delta.getY(), this.delta.getZ(), this.speed) > 0;
    }
}
