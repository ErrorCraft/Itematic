package net.errorcraft.itematic.mixin.world.scores.criteria;

import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ObjectiveCriteria.class)
public interface ObjectiveCriteriaAccessor {
    @Accessor("CRITERIA_CACHE")
    static Map<String, ObjectiveCriteria> criteriaCache() {
        throw new AssertionError();
    }
}
