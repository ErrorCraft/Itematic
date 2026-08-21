package net.errorcraft.itematic.world.scores.criteria;

import net.errorcraft.itematic.mixin.world.scores.criteria.ObjectiveCriteriaAccessor;
import net.errorcraft.itematic.resources.RegistryMapperCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.StatType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import java.util.Map;
import java.util.Optional;

public class ItematicObjectiveCriteria {
    private static final Map<String, ObjectiveCriteria> CRITERIA_CACHE = ObjectiveCriteriaAccessor.criteriaCache();

    private ItematicObjectiveCriteria() {}

    public static Optional<ObjectiveCriteria> byName(String name, RegistryMapperCodec.RegistryProvider registries) {
        ObjectiveCriteria cachedCriterion = CRITERIA_CACHE.get(name);
        if (cachedCriterion != null) {
            return Optional.of(cachedCriterion);
        }

        int separatorIndex = name.indexOf(':');
        if (separatorIndex == -1) {
            return Optional.empty();
        }

        return BuiltInRegistries.STAT_TYPE.getOptional(Identifier.bySeparator(name.substring(0, separatorIndex), '.'))
            .flatMap(statType -> getStat(
                statType,
                Identifier.bySeparator(name.substring(separatorIndex + 1), '.'),
                registries
            ));
    }

    private static <T> Optional<ObjectiveCriteria> getStat(StatType<T> statType, Identifier id, RegistryMapperCodec.RegistryProvider registries) {
        ResourceKey<? extends Registry<T>> registryId = statType.getRegistry().key();
        return registries.get(registryId)
            .flatMap(registry -> registry.get(ResourceKey.create(registryId, id)))
            .map(statType::itematic$get);
    }
}
