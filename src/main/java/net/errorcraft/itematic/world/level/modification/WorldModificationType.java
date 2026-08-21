package net.errorcraft.itematic.world.level.modification;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.level.modification.modifications.DrainFluidWorldModification;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceBlockWorldModification;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceFluidWorldModification;
import net.minecraft.core.Registry;

public record WorldModificationType<T extends WorldModification>(MapCodec<T> codec) {
    public static final WorldModificationType<DrainFluidWorldModification> DRAIN_FLUID = register(
        "drain_fluid",
        new WorldModificationType<>(DrainFluidWorldModification.CODEC)
    );
    public static final WorldModificationType<PlaceFluidWorldModification> PLACE_FLUID = register(
        "place_fluid",
        new WorldModificationType<>(PlaceFluidWorldModification.CODEC)
    );
    public static final WorldModificationType<PlaceBlockWorldModification> PLACE_BLOCK = register(
        "place_block",
        new WorldModificationType<>(PlaceBlockWorldModification.CODEC)
    );

    public static void init() {}

    private static <T extends WorldModification> WorldModificationType<T> register(String id, WorldModificationType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.WORLD_MODIFICATION_TYPE, id, type);
    }
}
