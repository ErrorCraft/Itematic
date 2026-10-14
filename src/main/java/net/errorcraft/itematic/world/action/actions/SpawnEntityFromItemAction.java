package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.component.EntityPlacerComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record SpawnEntityFromItemAction(PositionTarget position) implements Action<SpawnEntityFromItemAction> {
    public static final MapCodec<SpawnEntityFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SpawnEntityFromItemAction::position)
    ).apply(instance, SpawnEntityFromItemAction::new));

    public static SpawnEntityFromItemAction of(PositionTarget position) {
        return new SpawnEntityFromItemAction(position);
    }

    @Override
    public ActionType<SpawnEntityFromItemAction> type() {
        return ActionType.SPAWN_ENTITY_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        EntityPlacerComponent entityPlacer = context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY)
            .get(ItematicDataComponents.ENTITY_PLACER);
        if (entityPlacer == null) {
            return false;
        }

        return entityPlacer.place(context, this.position, null) != null;
    }
}
