package net.errorcraft.itematic.world.level.modification;

import net.errorcraft.itematic.world.level.modification.modifications.DrainFluid;
import net.errorcraft.itematic.world.level.modification.modifications.None;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceBlock;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceFluid;
import net.minecraft.core.Registry;

public class WorldModificationTypes {
    private WorldModificationTypes() {}

    public static void bootstrap(Registry<WorldModification.Type<?>> registry) {
        Registry.register(registry, "drain_fluid", DrainFluid.TYPE);
        Registry.register(registry, "place_fluid", PlaceFluid.TYPE);
        Registry.register(registry, "place_block", PlaceBlock.TYPE);
        Registry.register(registry, "none", None.TYPE);
    }
}
