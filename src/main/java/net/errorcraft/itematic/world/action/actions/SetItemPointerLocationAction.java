package net.errorcraft.itematic.world.action.actions;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.BlockPosUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public record SetItemPointerLocationAction(ActionContextParameter position) implements Action {
    public static final Codec<SetItemPointerLocationAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(SetItemPointerLocationAction::position)
    ).apply(instance, SetItemPointerLocationAction::new));
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public ActionType<?> type() {
        return ActionTypes.SET_ITEM_POINTER_LOCATION;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(this.position);
        ServerWorld world = context.world();
        world.playSound(null, pos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0f, 1.0f);
        ItemStack stack = context.stack();
        ItemStack resultStack = stack.split(1);
        setLocation(world.getRegistryKey(), pos, resultStack.getOrCreateNbt());
        PlayerEntity player = context.player(ActionContextParameter.THIS).orElse(null);
        context.setResultStack(getResultStack(player, stack, resultStack));
        return true;
    }

    private static void setLocation(RegistryKey<World> world, BlockPos pos, NbtCompound nbt) {
        BlockPosUtil.MAP_CODEC.encodeStart(NbtOps.INSTANCE, pos)
            .resultOrPartial(LOGGER::error)
            .ifPresent(nbtElement -> nbt.put(CompassItem.LODESTONE_POS_KEY, nbtElement));
        World.CODEC.encodeStart(NbtOps.INSTANCE, world)
            .resultOrPartial(LOGGER::error)
            .ifPresent(nbtElement -> nbt.put(CompassItem.LODESTONE_DIMENSION_KEY, nbtElement));
        nbt.putBoolean(CompassItem.LODESTONE_TRACKED_KEY, true);
    }

    private static ItemStack getResultStack(@Nullable PlayerEntity player, ItemStack currentStack, ItemStack newStack) {
        if (player == null) {
            return newStack;
        }
        return ItemUsage.exchangeStack(currentStack, player, newStack);
    }

    public static SetItemPointerLocationAction of(ActionContextParameter position) {
        return new SetItemPointerLocationAction(position);
    }
}
