package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;

public record PlaceBlockFromItemAction(PositionTarget position, boolean decrementCount) implements Action<PlaceBlockFromItemAction> {
    public static final MapCodec<PlaceBlockFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(PlaceBlockFromItemAction::position),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(PlaceBlockFromItemAction::decrementCount)
    ).apply(instance, PlaceBlockFromItemAction::new));

    public static PlaceBlockFromItemAction of(PositionTarget position, boolean decrementCount) {
        return new PlaceBlockFromItemAction(position, decrementCount);
    }

    @Override
    public ActionType<PlaceBlockFromItemAction> type() {
        return ActionTypes.PLACE_BLOCK_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockItemComponent block = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY)
            .itematic$getBehavior(ItemComponentTypes.BLOCK)
            .orElse(null);
        if (block == null) {
            return false;
        }

        BlockPlacer placer = BlockPlacer.action(context, this.position, block.block(), this.decrementCount);
        if (placer == null) {
            return false;
        }

        return placer.place().succeeds();
    }
}
