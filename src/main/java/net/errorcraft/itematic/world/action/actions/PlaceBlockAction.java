package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.registry.entry.RegistryEntry;

public record PlaceBlockAction(BlockPicker<?> block, PositionTarget position, boolean decrementCount) implements Action<PlaceBlockAction> {
    public static final MapCodec<PlaceBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(PlaceBlockAction::block),
        PositionTarget.CODEC.fieldOf("position").forGetter(PlaceBlockAction::position),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(PlaceBlockAction::decrementCount)
    ).apply(instance, PlaceBlockAction::new));

    public static PlaceBlockAction of(RegistryEntry<Block> block, PositionTarget position) {
        return new PlaceBlockAction(new SimpleBlockPicker(block), position, false);
    }

    @Override
    public ActionType<PlaceBlockAction> type() {
        return ActionTypes.PLACE_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPlacer placer = BlockPlacer.action(context, this.position, this.block, this.decrementCount);
        if (placer == null) {
            return false;
        }

        return placer.place().succeeds();
    }
}
