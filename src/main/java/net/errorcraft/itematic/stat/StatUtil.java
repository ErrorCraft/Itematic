package net.errorcraft.itematic.stat;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.mixin.stats.StatAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;

public class StatUtil {
    public static final Codec<Stat<?>> CODEC = BuiltInRegistries.STAT_TYPE.byNameCodec()
        .dispatch(Stat::getType, StatType::itematic$codec);
    private static final Identifier UNKNOWN = Identifier.withDefaultNamespace("unknown");

    private StatUtil() {}

    public static <T> String statName(StatType<T> stat, Holder<T> entry) {
        return StatAccessor.locationToKey(BuiltInRegistries.STAT_TYPE.getKey(stat)) + Identifier.NAMESPACE_SEPARATOR + StatAccessor.locationToKey(entry.unwrapKey().map(ResourceKey::identifier).orElse(UNKNOWN));
    }
}
