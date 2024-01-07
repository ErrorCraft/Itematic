package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.util.dynamic.Codecs;

public record PlaceBlockFromItemAction(ActionContextParameter position, boolean decrementCount) implements Action {
    public static final Codec<PlaceBlockFromItemAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(PlaceBlockFromItemAction::position),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "decrement_count", true).forGetter(PlaceBlockFromItemAction::decrementCount)
    ).apply(instance, PlaceBlockFromItemAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.PLACE_BLOCK_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.stack().itematic$getComponent(ItemComponentTypes.BLOCK)
            .map(component -> {
                PlaceBlockAction action = PlaceBlockAction.of(component.block(), this.position, this.decrementCount);
                return action.execute(context);
            }).orElse(false);
    }

    public static PlaceBlockFromItemAction of(ActionContextParameter position, boolean decrementCount) {
        return new PlaceBlockFromItemAction(position, decrementCount);
    }
}
