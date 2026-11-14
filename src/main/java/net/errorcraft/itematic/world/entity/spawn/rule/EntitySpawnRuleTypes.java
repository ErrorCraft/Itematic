package net.errorcraft.itematic.world.entity.spawn.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.AlignYaw;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.Discard;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.FitsInVolume;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPosition;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPositionByInteractedSide;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.RestsOnTopOfFace;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.SetSpawnPositionToInteractedPosition;
import net.minecraft.core.Registry;

public class EntitySpawnRuleTypes {
    private EntitySpawnRuleTypes() {}

    public static void bootstrap(Registry<MapCodec<? extends EntitySpawnRule<?>>> registry) {
        Registry.register(registry, "discard", Discard.CODEC);
        Registry.register(registry, "fits_in_volume", FitsInVolume.CODEC);
        Registry.register(registry, "align_yaw", AlignYaw.CODEC);
        Registry.register(registry, "offset_spawn_position", OffsetSpawnPosition.CODEC);
        Registry.register(registry, "rests_on_top_of_face", RestsOnTopOfFace.CODEC);
        Registry.register(registry, "set_spawn_position_to_interacted_position", SetSpawnPositionToInteractedPosition.CODEC);
        Registry.register(registry, "offset_spawn_position_by_interacted_side", OffsetSpawnPositionByInteractedSide.CODEC);
    }
}
