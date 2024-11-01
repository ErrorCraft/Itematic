package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.stat.StatUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.stat.Stat;

public record IncrementStatAction(ActionContextParameter entity, Stat<?> stat) implements Action<IncrementStatAction> {
    public static final MapCodec<IncrementStatAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(IncrementStatAction::entity),
        StatUtil.CODEC.fieldOf("stat").forGetter(IncrementStatAction::stat)
    ).apply(instance, IncrementStatAction::new));

    public static IncrementStatAction of(ActionContextParameter entity, Stat<?> stat) {
        return new IncrementStatAction(entity, stat);
    }

    @Override
    public ActionType<IncrementStatAction> type() {
        return ActionTypes.INCREMENT_STAT;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.player(this.entity)
            .map(player -> {
                player.incrementStat(this.stat);
                return true;
            })
            .orElse(false);
    }
}
