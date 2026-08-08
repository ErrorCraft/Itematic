package net.errorcraft.itematic.world.entity.spawn.rule.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.minecraft.util.Mth;

public record AlignYawEntitySpawnRule(int steps) implements EntitySpawnRule<AlignYawEntitySpawnRule> {
    public static final MapCodec<AlignYawEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.intRange(2, 360).fieldOf("steps").forGetter(AlignYawEntitySpawnRule::steps)
    ).apply(instance, AlignYawEntitySpawnRule::new));

    public static AlignYawEntitySpawnRule of(int steps) {
        return new AlignYawEntitySpawnRule(steps);
    }

    @Override
    public EntitySpawnRuleType<AlignYawEntitySpawnRule> type() {
        return EntitySpawnRuleType.ALIGN_YAW;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        float stepAngle = this.stepAngle();
        float angle = Mth.floor((Mth.wrapDegrees(context.userAngle() - 180.0f) + (stepAngle * 0.5f)) / stepAngle) * stepAngle;
        context.yaw(angle);
        return true;
    }

    private float stepAngle() {
        return 360.0f / this.steps;
    }
}
