package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.context.LootContext;

public record ClearStatusEffectsAction(LootContext.EntityTarget entity) implements Action<ClearStatusEffectsAction> {
    public static final MapCodec<ClearStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(ClearStatusEffectsAction::entity)
    ).apply(instance, ClearStatusEffectsAction::new));

    public static ClearStatusEffectsAction of(LootContext.EntityTarget entity) {
        return new ClearStatusEffectsAction(entity);
    }

    @Override
    public ActionType<ClearStatusEffectsAction> type() {
        return ActionTypes.CLEAR_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        return false;
    }

    @Override
    public boolean execute(NewActionContext context) {
        Entity entity = context.get(this.entity.getParameter());
        if (entity instanceof LivingEntity target) {
            return target.clearStatusEffects();
        }

        return false;
    }
}
