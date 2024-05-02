package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;

public record DamageItemAction(int amount, boolean ignoreGameMode) implements Action<DamageItemAction> {
    public static final MapCodec<DamageItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("amount").forGetter(DamageItemAction::amount),
        Codec.BOOL.optionalFieldOf("ignore_game_mode", false).forGetter(DamageItemAction::ignoreGameMode)
    ).apply(instance, DamageItemAction::new));

    public static DamageItemAction of(int amount) {
        return new DamageItemAction(amount, false);
    }

    @Override
    public ActionType<DamageItemAction> type() {
        return ActionTypes.DAMAGE_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.stack();
        if (stack.isEmpty()) {
            return false;
        }
        if (!stack.isDamageable()) {
            return false;
        }
        if (!this.preventDamage(context)) {
            stack.itematic$damage(this.amount, context);
        }
        return true;
    }

    private boolean preventDamage(ActionContext context) {
        return context.entity(ActionContextParameter.THIS).orElse(null) instanceof PlayerEntity player
            && player.isCreative()
            && !this.ignoreGameMode;
    }
}
