package net.errorcraft.itematic.world.level.modification.modifications;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.placement.fluid.FluidPlacer;
import net.errorcraft.itematic.world.level.material.Fluids;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public record PlaceFluid(Holder<Fluid> fluid, Holder<SoundEvent> placeSound, Holder<Item> transformsInto) implements WorldModification {
    public static final MapCodec<PlaceFluid> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Fluids.CODEC.fieldOf("fluid").forGetter(PlaceFluid::fluid),
        SoundEvent.CODEC.fieldOf("place_sound").forGetter(PlaceFluid::placeSound),
        Item.CODEC.fieldOf("transforms_into").forGetter(PlaceFluid::transformsInto)
    ).apply(instance, PlaceFluid::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlaceFluid> STREAM_CODEC = StreamCodec.composite(
        Fluids.STREAM_CODEC, PlaceFluid::fluid,
        SoundEvent.STREAM_CODEC, PlaceFluid::placeSound,
        Item.STREAM_CODEC, PlaceFluid::transformsInto,
        PlaceFluid::new
    );
    public static final Type<PlaceFluid> TYPE = new Type<>(CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
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
