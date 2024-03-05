package net.errorcraft.itematic.stat;

import net.errorcraft.itematic.mixin.stat.StatAccessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;

public class StatUtil {
    private static final Identifier UNKNOWN = new Identifier("unknown");

    private StatUtil() {}

    public static <T> String statName(StatType<T> stat, RegistryEntry<T> entry) {
        return StatAccessor.getName(Registries.STAT_TYPE.getId(stat)) + Identifier.NAMESPACE_SEPARATOR + StatAccessor.getName(entry.getKey().map(RegistryKey::getValue).orElse(UNKNOWN));
    }
}
