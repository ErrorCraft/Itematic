package net.errorcraft.itematic.world.entity.spawn.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.AlignYawEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.DiscardEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.FitsInVolumeEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPositionEntitySpawnRule;
import net.minecraft.core.Registry;

public record EntitySpawnRuleType<T extends EntitySpawnRule<T>>(MapCodec<T> codec) {
    public static final EntitySpawnRuleType<DiscardEntitySpawnRule> DISCARD = register(
        "discard",
        new EntitySpawnRuleType<>(DiscardEntitySpawnRule.CODEC)
    );
    public static final EntitySpawnRuleType<FitsInVolumeEntitySpawnRule> FITS_IN_VOLUME = register(
        "fits_in_volume",
        new EntitySpawnRuleType<>(FitsInVolumeEntitySpawnRule.CODEC)
    );
    public static final EntitySpawnRuleType<AlignYawEntitySpawnRule> ALIGN_YAW = register(
        "align_yaw",
        new EntitySpawnRuleType<>(AlignYawEntitySpawnRule.CODEC)
    );
    public static final EntitySpawnRuleType<OffsetSpawnPositionEntitySpawnRule> OFFSET_SPAWN_POSITION = register(
        "offset_spawn_position",
        new EntitySpawnRuleType<>(OffsetSpawnPositionEntitySpawnRule.CODEC)
    );

    public static void init() {}

    private static <T extends EntitySpawnRule<T>> EntitySpawnRuleType<T> register(String id, EntitySpawnRuleType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.ENTITY_SPAWN_RULE_TYPE, id, type);
    }
}
