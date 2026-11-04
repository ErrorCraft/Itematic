package net.errorcraft.itematic.world.level.modification.modifications;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.placement.block.BlockPlacer;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

import java.util.Optional;

public record PlaceBlock(BlockPicker<?> block, Holder<SoundEvent> placeSound, Holder<Item> transformsInto) implements WorldModification {
    public static final MapCodec<PlaceBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(PlaceBlock::block),
        SoundEvent.CODEC.fieldOf("place_sound").forGetter(PlaceBlock::placeSound),
        Item.CODEC.fieldOf("transforms_into").forGetter(PlaceBlock::transformsInto)
    ).apply(instance, PlaceBlock::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlaceBlock> STREAM_CODEC = StreamCodec.composite(
        BlockPicker.STREAM_CODEC, PlaceBlock::block,
        SoundEvent.STREAM_CODEC, PlaceBlock::placeSound,
        Item.STREAM_CODEC, PlaceBlock::transformsInto,
        PlaceBlock::new
    );
    public static final Type<PlaceBlock> TYPE = new Type<>(CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset) {
        BlockPlacer placer = BlockPlacer.of(
            context,
            position,
            this.block,
            false,
            this.placeSound
        );

        if (placer == null || !placer.place()) {
            return Optional.empty();
        }

        return Optional.of(new ItemStack(this.transformsInto));
    }

    @Override
    public ClipContext.Fluid fluidHandling() {
        return ClipContext.Fluid.NONE;
    }
}
