package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;

public record SwingHandAction(LootContext.EntityTarget entity) implements Action<SwingHandAction> {
    public static final MapCodec<SwingHandAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(SwingHandAction::entity)
    ).apply(instance, SwingHandAction::new));

    public static SwingHandAction of(LootContext.EntityTarget entity) {
        return new SwingHandAction(entity);
    }

    @Override
    public ActionType<SwingHandAction> type() {
        return ActionType.SWING_HAND;
    }

    @Override
    public boolean execute(ActionContext context) {
        Entity entity = context.get(this.entity.contextParam());
        if (!(entity instanceof LivingEntity target)) {
            return false;
        }

        InteractionHand hand = context.get(ItematicContextKeys.HAND);
        if (hand == null) {
            return false;
        }

        target.swing(hand, true);
        return true;
    }
}
