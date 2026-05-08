package net.errorcraft.itematic.item.bucket.modification;

import net.errorcraft.itematic.item.bucket.modification.type.DrainFluid;
import net.errorcraft.itematic.item.bucket.modification.type.PlaceBlock;
import net.errorcraft.itematic.item.bucket.modification.type.PlaceFluid;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;

public class WorldModificationTypes {
    public static final WorldModificationType<DrainFluid> DRAIN_FLUID = register("drain_fluid", new WorldModificationType<>(DrainFluid.CODEC));
    public static final WorldModificationType<PlaceFluid> PLACE_FLUID = register("place_fluid", new WorldModificationType<>(PlaceFluid.CODEC));
    public static final WorldModificationType<PlaceBlock> PLACE_BLOCK = register("place_block", new WorldModificationType<>(PlaceBlock.CODEC));

    private WorldModificationTypes() {}

    public static void init() {}

    private static <T extends WorldModification> WorldModificationType<T> register(String id, WorldModificationType<T> type) {
        return Registry.register(ItematicRegistries.WORLD_MODIFICATION_TYPE, id, type);
    }
}
