package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record SetItemPointerLocationAction(ActionContextParameter position) implements Action<SetItemPointerLocationAction> {
    public static final MapCodec<SetItemPointerLocationAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(SetItemPointerLocationAction::position)
    ).apply(instance, SetItemPointerLocationAction::new));

    public static SetItemPointerLocationAction of(ActionContextParameter position) {
        return new SetItemPointerLocationAction(position);
    }

    @Override
    public ActionType<SetItemPointerLocationAction> type() {
        return ActionTypes.SET_ITEM_POINTER_LOCATION;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(this.position);
        ServerWorld world = context.world();
        world.playSound(null, pos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0f, 1.0f);
        ItemStack stack = context.stack();
        ItemStack resultStack = stack.split(1);
        resultStack.set(DataComponentTypes.LODESTONE_TRACKER, new LodestoneTrackerComponent(Optional.of(GlobalPos.create(world.getRegistryKey(), pos)), true));
        PlayerEntity player = context.player(ActionContextParameter.THIS).orElse(null);
        context.setResultStack(getResultStack(player, stack, resultStack));
        return true;
    }

    private static ItemStack getResultStack(@Nullable PlayerEntity player, ItemStack currentStack, ItemStack newStack) {
        if (player == null) {
            return newStack;
        }
        return ItemUsage.exchangeStack(currentStack, player, newStack);
    }
}
