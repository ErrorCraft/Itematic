package net.errorcraft.itematic.world.modification;

import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.modification.type.DrainFluidWorldModification;
import net.errorcraft.itematic.world.modification.type.PlaceBlockWorldModification;
import net.errorcraft.itematic.world.modification.type.PlaceFluidWorldModification;
import net.minecraft.registry.Registry;

public class WorldModificationTypes {
    public static final WorldModificationType<DrainFluidWorldModification> DRAIN_FLUID = register("drain_fluid", new WorldModificationType<>(DrainFluidWorldModification.CODEC));
    public static final WorldModificationType<PlaceFluidWorldModification> PLACE_FLUID = register("place_fluid", new WorldModificationType<>(PlaceFluidWorldModification.CODEC));
    public static final WorldModificationType<PlaceBlockWorldModification> PLACE_BLOCK = register("place_block", new WorldModificationType<>(PlaceBlockWorldModification.CODEC));

    private WorldModificationTypes() {}

    public static void init() {}

    private static <T extends WorldModification> WorldModificationType<T> register(String id, WorldModificationType<T> type) {
        return Registry.register(ItematicRegistries.WORLD_MODIFICATION_TYPE, id, type);
    }
}
