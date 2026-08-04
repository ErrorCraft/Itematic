package net.errorcraft.itematic.world.modification.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.fluid.FluidPlacer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.modification.WorldModification;
import net.errorcraft.itematic.world.modification.WorldModificationType;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluid;
import java.util.Optional;

public record PlaceFluidWorldModification(Holder<Fluid> fluid, Holder<SoundEvent> placeSound, Holder<Item> transformsInto) implements WorldModification {
    public static final MapCodec<PlaceFluidWorldModification> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.create(Registries.FLUID).fieldOf("fluid").forGetter(PlaceFluidWorldModification::fluid),
        SoundEvent.CODEC.fieldOf("place_sound").forGetter(PlaceFluidWorldModification::placeSound),
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("transforms_into").forGetter(PlaceFluidWorldModification::transformsInto)
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
    public ClipContext.Fluid fluidHandling() {
        return ClipContext.Fluid.NONE;
    }
}
