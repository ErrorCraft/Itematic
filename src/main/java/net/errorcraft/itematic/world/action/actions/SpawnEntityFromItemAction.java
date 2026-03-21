package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;

public record SpawnEntityFromItemAction(PositionTarget position) implements Action<SpawnEntityFromItemAction> {
    public static final MapCodec<SpawnEntityFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SpawnEntityFromItemAction::position)
    ).apply(instance, SpawnEntityFromItemAction::new));

    public static SpawnEntityFromItemAction of(PositionTarget position) {
        return new SpawnEntityFromItemAction(position);
    }

    @Override
    public ActionType<SpawnEntityFromItemAction> type() {
        return ActionTypes.SPAWN_ENTITY_FROM_ITEM;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        return stack.itematic$getBehavior(ItemComponentTypes.ENTITY)
            .map(entity -> EntityPlacer.action(
                context,
                this.position,
                entity.getEntityInitializer(stack, context.world().getRegistryManager()))
            )
            .map(EntityPlacer::place)
            .map(ItemResult::succeeds)
            .orElse(false);
    }
}
