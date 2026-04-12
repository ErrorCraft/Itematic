package net.errorcraft.itematic.mixin.scoreboard;

import net.minecraft.scoreboard.ScoreboardCriterion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ScoreboardCriterion.class)
public interface ScoreboardCriterionAccessor {
    @Accessor("CRITERIA")
    static Map<String, ScoreboardCriterion> customCriteria() {
        throw new AssertionError();
    }
}
