package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.util.Mth;

public record AlignYawEntitySpawnRule(int steps, boolean faceAwayFromPlacer) implements EntitySpawnRule<AlignYawEntitySpawnRule> {
    public static final MapCodec<AlignYawEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.intRange(2, 360).fieldOf("steps").forGetter(AlignYawEntitySpawnRule::steps),
        Codec.BOOL.optionalFieldOf("face_away_from_player", false).forGetter(AlignYawEntitySpawnRule::faceAwayFromPlacer)
    ).apply(instance, AlignYawEntitySpawnRule::new));

    public static AlignYawEntitySpawnRule ofFacingAway(int steps) {
        return new AlignYawEntitySpawnRule(steps, true);
    }

    public static AlignYawEntitySpawnRule ofFacingTowards(int steps) {
        return new AlignYawEntitySpawnRule(steps, false);
    }

    @Override
    public MapCodec<AlignYawEntitySpawnRule> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        float placerAngle = context.userAngle();
        float angle = Mth.wrapDegrees(
            this.faceAwayFromPlacer
                ? placerAngle - 180.0f
                : placerAngle
        );
        float stepAngle = 360.0f / this.steps;
        float alignedAngle = Mth.floor((angle + (stepAngle * 0.5f)) / stepAngle) * stepAngle;
        context.yaw(alignedAngle);
        return true;
    }
}
