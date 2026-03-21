package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.SaddleItemComponent;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Saddleable;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public record SaddleEntityAtPositionAction(PositionTarget position) implements Action<SaddleEntityAtPositionAction> {
    public static final MapCodec<SaddleEntityAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SaddleEntityAtPositionAction::position)
    ).apply(instance, SaddleEntityAtPositionAction::new));

    public static SaddleEntityAtPositionAction of(PositionTarget position) {
        return new SaddleEntityAtPositionAction(position);
    }

    @Override
    public ActionType<SaddleEntityAtPositionAction> type() {
        return ActionTypes.SADDLE_ENTITY_AT_POSITION;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        return stack.itematic$getBehavior(ItemComponentTypes.SADDLE)
            .map(saddle -> this.trySaddle(saddle, stack, context))
            .orElse(false);
    }

    private boolean trySaddle(SaddleItemComponent saddle, ItemStack stack, NewActionContext context) {
        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        ServerWorld world = context.world();
        List<LivingEntity> targets = world.getEntitiesByClass(
            LivingEntity.class,
            new Box(pos),
            entity -> entity instanceof Saddleable
        );
        for (LivingEntity target : targets) {
            if (saddle.trySaddle(target, world, stack, SoundCategory.BLOCKS)) {
                return true;
            }
        }

        return false;
    }
}
