package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public record SetEntityNameFromItemAction(ActionContextParameter entity) implements Action<SetEntityNameFromItemAction> {
    public static final MapCodec<SetEntityNameFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(SetEntityNameFromItemAction::entity)
    ).apply(instance, SetEntityNameFromItemAction::new));

    public static SetEntityNameFromItemAction of(ActionContextParameter entity) {
        return new SetEntityNameFromItemAction(entity);
    }

    @Override
    public ActionType<SetEntityNameFromItemAction> type() {
        return ActionTypes.SET_ENTITY_NAME_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        Text customName = context.stack().get(DataComponentTypes.CUSTOM_NAME);
        if (customName == null) {
            return false;
        }
        Entity entity = context.entity(this.entity).orElse(null);
        if (entity == null || entity instanceof PlayerEntity) {
            return false;
        }
        if (entity.isAlive()) {
            entity.setCustomName(customName);
            if (entity instanceof MobEntity mobEntity) {
                mobEntity.setPersistent();
            }
        }
        return true;
    }
}
