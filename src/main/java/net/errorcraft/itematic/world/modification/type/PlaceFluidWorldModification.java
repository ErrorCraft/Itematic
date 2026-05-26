package net.errorcraft.itematic.world.modification.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.fluid.FluidPlacer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.modification.WorldModification;
import net.errorcraft.itematic.world.modification.WorldModificationType;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public record PlaceFluidWorldModification(RegistryEntry<Fluid> fluid, RegistryEntry<SoundEvent> placeSound, RegistryEntry<Item> transformsInto) implements WorldModification {
    public static final MapCodec<PlaceFluidWorldModification> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.FLUID).fieldOf("fluid").forGetter(PlaceFluidWorldModification::fluid),
        SoundEvent.ENTRY_CODEC.fieldOf("place_sound").forGetter(PlaceFluidWorldModification::placeSound),
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("transforms_into").forGetter(PlaceFluidWorldModification::transformsInto)
    ).apply(instance, PlaceFluidWorldModification::new));

    @Override
    public WorldModificationType<?> type() {
        return WorldModificationTypes.PLACE_FLUID;
    }

    @Override
    public Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset) {
        FluidPlacer placer = new FluidPlacer(
            context,
            position,
            this.fluid,
            this.placeSound,
            mayOffset
        );
        if (placer.place()) {
            return Optional.of(new ItemStack(this.transformsInto));
        }

        return Optional.empty();
    }

    @Override
    public RaycastContext.FluidHandling fluidHandling() {
        return RaycastContext.FluidHandling.NONE;
    }
}
