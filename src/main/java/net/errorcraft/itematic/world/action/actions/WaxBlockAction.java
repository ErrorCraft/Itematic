package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

public record WaxBlockAction(PositionTarget position) implements Action<WaxBlockAction> {
    public static final MapCodec<WaxBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(WaxBlockAction::position)
    ).apply(instance, WaxBlockAction::new));

    public static WaxBlockAction of(PositionTarget position) {
        return new WaxBlockAction(position);
    }

    @Override
    public ActionType<WaxBlockAction> type() {
        return ActionType.WAX_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level level = context.level();
        BlockState oldState = level.getBlockState(pos);
        return HoneycombItem.getWaxed(oldState)
            .map(waxedState -> {
                Entity entity = context.get(LootContextParams.THIS_ENTITY);
                level.setBlock(pos, waxedState, Block.UPDATE_ALL_IMMEDIATE);
                waxedAt(level, entity, pos, waxedState);
                if (oldState.getBlock() instanceof ChestBlock && oldState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
                    BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldState);
                    waxedAt(level, entity, neighborPos, level.getBlockState(neighborPos));
                }

                return true;
            })
            .orElse(false);
    }

    private static void waxedAt(Level level, @Nullable Entity entity, BlockPos pos, BlockState waxedState) {
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, waxedState));
        level.levelEvent(entity, LevelEvent.PARTICLES_WAX_ON, pos, 0);
        level.playSound(entity, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
}
