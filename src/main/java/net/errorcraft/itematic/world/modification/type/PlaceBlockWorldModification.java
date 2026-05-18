package net.errorcraft.itematic.world.modification.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.block.BlockPlacer;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.modification.WorldModification;
import net.errorcraft.itematic.world.modification.WorldModificationType;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public record PlaceBlockWorldModification(BlockPicker<?> block, RegistryEntry<SoundEvent> placeSound, RegistryEntry<Item> transformsInto) implements WorldModification {
    public static final MapCodec<PlaceBlockWorldModification> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(PlaceBlockWorldModification::block),
        SoundEvent.ENTRY_CODEC.fieldOf("place_sound").forGetter(PlaceBlockWorldModification::placeSound),
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("transforms_into").forGetter(PlaceBlockWorldModification::transformsInto)
    ).apply(instance, PlaceBlockWorldModification::new));

    @Override
    public WorldModificationType<?> type() {
        return WorldModificationTypes.PLACE_BLOCK;
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

        if (!placer.place()) {
            return Optional.empty();
        }

        return Optional.of(new ItemStack(this.transformsInto));
    }

    @Override
    public RaycastContext.FluidHandling fluidHandling() {
        return RaycastContext.FluidHandling.NONE;
    }
}
