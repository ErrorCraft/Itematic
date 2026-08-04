package net.errorcraft.itematic.scoreboard;

import net.errorcraft.itematic.mixin.scoreboard.ScoreboardCriterionAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.StatType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import java.util.Map;
import java.util.Optional;

public class ScoreboardCriterionUtil {
    private static final Map<String, ObjectiveCriteria> CUSTOM_CRITERIA = ScoreboardCriterionAccessor.customCriteria();

    private ScoreboardCriterionUtil() {}

    public static Optional<ObjectiveCriteria> byName(String name, RegistryOps<?> ops) {
        ObjectiveCriteria customCriterion = CUSTOM_CRITERIA.get(name);
        if (customCriterion != null) {
            return Optional.of(customCriterion);
        }

        int separatorIndex = name.indexOf(':');
        if (separatorIndex == -1) {
            return Optional.empty();
        }

        return BuiltInRegistries.STAT_TYPE.getOptional(Identifier.bySeparator(name.substring(0, separatorIndex), '.'))
            .flatMap(statType -> getStat(
                statType,
                Identifier.bySeparator(name.substring(separatorIndex + 1), '.'),
                ops
            ));
    }

    private static <T> Optional<ObjectiveCriteria> getStat(StatType<T> statType, Identifier id, RegistryOps<?> ops) {
        ResourceKey<? extends Registry<T>> registryKey = statType.getRegistry().key();
        return ops.getter(registryKey)
            .flatMap(lookup -> lookup.get(ResourceKey.create(registryKey, id)))
            .map(statType::itematic$getOrCreateStat);
    }
}
