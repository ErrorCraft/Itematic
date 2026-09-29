package net.errorcraft.itematic.world.entity.spawn.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.AlignYawEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.DiscardEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.FitsInVolumeEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPositionEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPositionToExactYEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.RestsOnTopOfFaceEntitySpawnRule;
import net.minecraft.core.Registry;

public class EntitySpawnRuleTypes {
    private EntitySpawnRuleTypes() {}

    public static void bootstrap(Registry<MapCodec<? extends EntitySpawnRule<?>>> registry) {
        Registry.register(registry, "discard", DiscardEntitySpawnRule.CODEC);
        Registry.register(registry, "fits_in_volume", FitsInVolumeEntitySpawnRule.CODEC);
        Registry.register(registry, "align_yaw", AlignYawEntitySpawnRule.CODEC);
        Registry.register(registry, "offset_spawn_position", OffsetSpawnPositionEntitySpawnRule.CODEC);
        Registry.register(registry, "rests_on_top_of_face", RestsOnTopOfFaceEntitySpawnRule.CODEC);
        Registry.register(registry, "offset_spawn_position_to_exact_y", OffsetSpawnPositionToExactYEntitySpawnRule.CODEC);
    }
}
