package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record PlaceBlockAction(RegistryEntry<Block> block, ActionContextParameter position, boolean decrementCount) implements Action {
    public static final Codec<PlaceBlockAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.BLOCK).fieldOf("block").forGetter(PlaceBlockAction::block),
        ActionContextParameter.CODEC.fieldOf("position").forGetter(PlaceBlockAction::position),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(PlaceBlockAction::decrementCount)
    ).apply(instance, PlaceBlockAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.PLACE_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPlacer placer = BlockPlacer.of(context, this.position, this.block, false, this.decrementCount);
        return placer.place().isAccepted();
    }
}
