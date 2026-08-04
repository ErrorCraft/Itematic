package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record DecrementItemAction(int amount, boolean ignoreGameMode) implements Action<DecrementItemAction> {
    public static final MapCodec<DecrementItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(DecrementItemAction::amount),
        Codec.BOOL.optionalFieldOf("ignore_game_mode", false).forGetter(DecrementItemAction::ignoreGameMode)
    ).apply(instance, DecrementItemAction::new));

    public static DecrementItemAction of(int amount) {
        return new DecrementItemAction(amount, false);
    }

    @Override
    public ActionType<DecrementItemAction> type() {
        return ActionTypes.DECREMENT_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParams.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        if (this.ignoreGameMode) {
            stack.shrink(this.amount);
        } else {
            Entity entity = context.get(LootContextParams.THIS_ENTITY);
            stack.consume(
                this.amount,
                entity instanceof LivingEntity livingEntity ? livingEntity : null
            );
        }

        return true;
    }
}
