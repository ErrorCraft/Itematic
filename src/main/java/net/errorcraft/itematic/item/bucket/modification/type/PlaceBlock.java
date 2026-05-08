package net.errorcraft.itematic.item.bucket.modification.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.bucket.modification.WorldModification;
import net.errorcraft.itematic.item.bucket.modification.WorldModificationType;
import net.errorcraft.itematic.item.bucket.modification.WorldModificationTypes;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public record PlaceBlock(BlockPicker<?> block, RegistryEntry<SoundEvent> placeSound, RegistryEntry<Item> transformsInto) implements WorldModification {
    public static final MapCodec<PlaceBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(PlaceBlock::block),
        SoundEvent.ENTRY_CODEC.fieldOf("place_sound").forGetter(PlaceBlock::placeSound),
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("transforms_into").forGetter(PlaceBlock::transformsInto)
    ).apply(instance, PlaceBlock::new));

    @Override
    public WorldModificationType<?> type() {
        return WorldModificationTypes.PLACE_BLOCK;
    }

    @Override
    public Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset) {
        BlockPlacer placer = BlockPlacer.action(
            context,
            PositionTarget.INTERACTED_POSITION,
            this.block,
            false,
            this.placeSound
        );
        if (placer == null) {
            return Optional.empty();
        }

        if (!placer.place().succeeds()) {
            return Optional.empty();
        }

        return Optional.of(new ItemStack(this.transformsInto));
    }

    @Override
    public RaycastContext.FluidHandling fluidHandling() {
        return RaycastContext.FluidHandling.NONE;
    }
}
