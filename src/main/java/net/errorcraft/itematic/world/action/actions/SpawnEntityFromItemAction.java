package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;

public record SpawnEntityFromItemAction(ActionContextParameter position) implements Action<SpawnEntityFromItemAction> {
    public static final MapCodec<SpawnEntityFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(SpawnEntityFromItemAction::position)
    ).apply(instance, SpawnEntityFromItemAction::new));

    public static SpawnEntityFromItemAction of(ActionContextParameter position) {
        return new SpawnEntityFromItemAction(position);
    }

    @Override
    public ActionType<SpawnEntityFromItemAction> type() {
        return ActionTypes.SPAWN_ENTITY_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.stack()
            .itematic$getComponent(ItemComponentTypes.ENTITY)
            .map(itemComponent -> EntityPlacer.action(context, this.position, itemComponent)
                .place()
                .succeeds())
            .orElse(false);
    }
}
