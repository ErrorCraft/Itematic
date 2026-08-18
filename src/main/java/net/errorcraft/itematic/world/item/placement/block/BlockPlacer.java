package net.errorcraft.itematic.world.item.placement.block;

import net.errorcraft.itematic.mixin.world.item.BlockItemAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.world.phys.shapes.CollisionContexts;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

public class BlockPlacer {
    private final ActionContext context;
    private final BlockPicker<?> block;
    private final BlockPlaceContext placementContext;
    private final boolean operatorOnly;
    @Nullable
    private final Holder<SoundEvent> placeSound;

    private BlockPlacer(ActionContext context, BlockPicker<?> block, BlockPlaceContext placementContext, boolean operatorOnly, @Nullable Holder<SoundEvent> placeSound) {
        this.context = context;
        this.block = block;
        this.placementContext = placementContext;
        this.operatorOnly = operatorOnly;
        this.placeSound = placeSound;
    }

    public static BlockPlacer of(ActionContext context, PositionTarget position, BlockPicker<?> block, boolean operatorOnly, Holder<SoundEvent> placeSound) {
        return new BlockPlacer(
            context,
            block,
            context.blockPlaceContext(position, block),
            operatorOnly,
            placeSound
        );
    }

    public boolean place() {
        if (!this.placementContext.canPlace()) {
            return false;
        }

        BlockPos pos = this.placementContext.getClickedPos();
        LivingEntity placer = this.context.get(LootContextParams.THIS_ENTITY, LivingEntity.class);
        BlockState blockState = this.getPlacementState(pos, placer);
        if (blockState == null) {
            return false;
        }

        if (!this.context.world().setBlock(pos, blockState, Block.UPDATE_ALL_IMMEDIATE)) {
            return false;
        }

        this.placed(blockState, pos, placer);
        return true;
    }

    private void placed(BlockState blockState, BlockPos pos, @Nullable LivingEntity placer) {
        Level world = this.context.world();
        ItemStack stack = this.context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY);
        blockState = this.placeFromNbt(blockState, pos, stack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null && placer instanceof Player playerPlacer) {
            blockEntity.itematic$placedFromItemStack(world, playerPlacer, blockState, pos, stack);
        }

        BlockItemAccessor.updateBlockEntityComponents(world, pos, stack);
        blockState.getBlock().setPlacedBy(world, pos, blockState, placer, stack);
        if (placer instanceof ServerPlayer playerPlacer) {
            CriteriaTriggers.PLACED_BLOCK.trigger(playerPlacer, pos, stack);
        }

        stack.itematic$invokeEvent(ItemEvent.PLACED_BLOCK, this.context);
        SoundType blockSoundGroup = blockState.getSoundType();
        world.playSound(placer, pos, this.placeSound(blockSoundGroup), SoundSource.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
        world.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(placer, blockState));
    }

    private SoundEvent placeSound(SoundType group) {
        if (this.placeSound != null) {
            return this.placeSound.value();
        }

        return group.getPlaceSound();
    }

    @Nullable
    private BlockState getPlacementState(BlockPos pos, @Nullable LivingEntity placer) {
        if (this.operatorOnly && placer instanceof Player playerPlacer && !playerPlacer.canUseGameMasterBlocks()) {
            return null;
        }

        BlockState state = this.block.placementState(this.placementContext);
        return this.canPlace(state, pos, placer) ? state : null;
    }

    private boolean canPlace(BlockState state, BlockPos pos, @Nullable LivingEntity placer) {
        if (state == null) {
            return false;
        }

        CollisionContext shapeContext = CollisionContexts.ofNullable(placer);
        Level world = this.context.world();
        return state.canSurvive(world, pos) &&
            world.isUnobstructed(state, pos, shapeContext);
    }

    private BlockState placeFromNbt(BlockState state, BlockPos pos, ItemStack stack) {
        BlockItemStateProperties blockStateProperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        if (blockStateProperties.isEmpty()) {
            return state;
        }

        BlockState modifiedState = blockStateProperties.apply(state);
        if (modifiedState != state) {
            this.context.world().setBlock(pos, modifiedState, Block.UPDATE_CLIENTS);
        }

        return modifiedState;
    }
}
