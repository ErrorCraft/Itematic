package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Saddleable;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public record SaddleEntityAtPositionAction(ActionContextParameter position) implements Action<SaddleEntityAtPositionAction> {
    public static final MapCodec<SaddleEntityAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(SaddleEntityAtPositionAction::position)
    ).apply(instance, SaddleEntityAtPositionAction::new));

    public static SaddleEntityAtPositionAction of(ActionContextParameter position) {
        return new SaddleEntityAtPositionAction(position);
    }

    @Override
    public ActionType<SaddleEntityAtPositionAction> type() {
        return ActionTypes.SADDLE_ENTITY_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.stack();
        return stack.itematic$getComponent(ItemComponentTypes.SADDLE)
            .map(c -> {
                BlockPos position = context.blockPos(this.position);
                ServerWorld world = context.world();
                List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, new Box(position), entity -> entity instanceof Saddleable);
                for (LivingEntity target : targets) {
                    if (c.trySaddle(target, world, stack, SoundCategory.BLOCKS)) {
                        return true;
                    }
                }
                return false;
            })
            .orElse(false);
    }
}
