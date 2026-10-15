package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.component.BlockPlacerComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

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
        return ActionType.PLACE_BLOCK_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPlacerComponent blockPlacer = context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY)
            .get(ItematicDataComponents.BLOCK_PLACER);
        if (blockPlacer == null) {
            return false;
        }

        return blockPlacer.place(context, this.position, this.decrementCount);
    }
}
