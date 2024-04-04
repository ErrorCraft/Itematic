package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;

public record ShootProjectileFromItemAction(ActionContextParameter position, float power, float uncertainty) implements Action<ShootProjectileFromItemAction> {
    public static final MapCodec<ShootProjectileFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(ShootProjectileFromItemAction::position),
        Codec.FLOAT.fieldOf("power").forGetter(ShootProjectileFromItemAction::power),
        Codec.FLOAT.fieldOf("uncertainty").forGetter(ShootProjectileFromItemAction::uncertainty)
    ).apply(instance, ShootProjectileFromItemAction::new));

    public static ShootProjectileFromItemAction of(ActionContextParameter position, float power, float uncertainty) {
        return new ShootProjectileFromItemAction(position, power, uncertainty);
    }

    @Override
    public ActionType<ShootProjectileFromItemAction> type() {
        return ActionTypes.SHOOT_PROJECTILE_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.stack()
            .itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(c -> c.createEntity(context, this.position, 0.0f, this.power, this.uncertainty))
            .map(entity -> {
                context.world().spawnEntity(entity);
                return true;
            })
            .orElse(false);
    }
}
