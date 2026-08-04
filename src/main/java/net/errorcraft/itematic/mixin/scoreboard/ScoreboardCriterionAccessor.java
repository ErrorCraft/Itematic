package net.errorcraft.itematic.mixin.scoreboard;

import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ObjectiveCriteria.class)
public interface ScoreboardCriterionAccessor {
    @Accessor("CRITERIA_CACHE")
    static Map<String, ObjectiveCriteria> customCriteria() {
        throw new AssertionError();
    }
}
