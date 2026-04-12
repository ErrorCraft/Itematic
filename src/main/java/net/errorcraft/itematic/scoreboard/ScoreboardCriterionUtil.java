package net.errorcraft.itematic.scoreboard;

import net.errorcraft.itematic.mixin.scoreboard.ScoreboardCriterionAccessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class ScoreboardCriterionUtil {
    private static final Map<String, ScoreboardCriterion> CUSTOM_CRITERIA = ScoreboardCriterionAccessor.customCriteria();

    private ScoreboardCriterionUtil() {}

    public static Optional<ScoreboardCriterion> byName(String name, RegistryOps<?> ops) {
        ScoreboardCriterion customCriterion = CUSTOM_CRITERIA.get(name);
        if (customCriterion != null) {
            return Optional.of(customCriterion);
        }

        int separatorIndex = name.indexOf(':');
        if (separatorIndex == -1) {
            return Optional.empty();
        }

        return Registries.STAT_TYPE.getOptionalValue(Identifier.splitOn(name.substring(0, separatorIndex), '.'))
            .flatMap(statType -> getStat(
                statType,
                Identifier.splitOn(name.substring(separatorIndex + 1), '.'),
                ops
            ));
    }

    private static <T> Optional<ScoreboardCriterion> getStat(StatType<T> statType, Identifier id, RegistryOps<?> ops) {
        RegistryKey<? extends Registry<T>> registryKey = statType.getRegistry().getKey();
        return ops.getEntryLookup(registryKey)
            .flatMap(lookup -> lookup.getOptional(RegistryKey.of(registryKey, id)))
            .map(statType::itematic$getOrCreateStat);
    }
}
