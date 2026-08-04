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
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import java.util.Optional;

public record PlaceBlockWorldModification(BlockPicker<?> block, Holder<SoundEvent> placeSound, Holder<Item> transformsInto) implements WorldModification {
    public static final MapCodec<PlaceBlockWorldModification> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(PlaceBlockWorldModification::block),
        SoundEvent.CODEC.fieldOf("place_sound").forGetter(PlaceBlockWorldModification::placeSound),
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("transforms_into").forGetter(PlaceBlockWorldModification::transformsInto)
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
    public ClipContext.Fluid fluidHandling() {
        return ClipContext.Fluid.NONE;
    }
}
