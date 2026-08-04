package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.stat.StatUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;

public record IncrementStatAction(LootContext.EntityTarget entity, Stat<?> stat) implements Action<IncrementStatAction> {
    public static final MapCodec<IncrementStatAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(IncrementStatAction::entity),
        StatUtil.CODEC.fieldOf("stat").forGetter(IncrementStatAction::stat)
    ).apply(instance, IncrementStatAction::new));

    public static IncrementStatAction of(LootContext.EntityTarget entity, Stat<?> stat) {
        return new IncrementStatAction(entity, stat);
    }

    @Override
    public ActionType<IncrementStatAction> type() {
        return ActionTypes.INCREMENT_STAT;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (context.get(this.entity.contextParam()) instanceof Player player) {
            player.awardStat(this.stat);
            return true;
        }

        return false;
    }
}
