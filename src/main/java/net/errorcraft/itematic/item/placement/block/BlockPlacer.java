package net.errorcraft.itematic.item.placement.block;

import net.errorcraft.itematic.block.ShapeContexts;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.mixin.block.BlockItemAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BlockPlacer {
    private final ActionContext context;
    private final BlockPicker<?> block;
    private final ItemPlacementContext placementContext;
    private final boolean operatorOnly;
    @Nullable
    private final RegistryEntry<SoundEvent> placeSound;

    public BlockPlacer(ActionContext context, PositionTarget position, BlockPicker<?> block, ItemPlacementContext placementContext, boolean operatorOnly, @Nullable RegistryEntry<SoundEvent> placeSound) {
        this.context = context;
        this.block = block;
        this.placementContext = placementContext;
        this.operatorOnly = operatorOnly;
        this.placeSound = placeSound;
    }

    public static BlockPlacer of(ActionContext context, PositionTarget position, BlockPicker<?> block, boolean operatorOnly, RegistryEntry<SoundEvent> placeSound) {
        return new BlockPlacer(
            context,
            position,
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

        BlockPos pos = this.placementContext.getBlockPos();
        LivingEntity placer = this.context.get(LootContextParameters.THIS_ENTITY, LivingEntity.class);
        BlockState blockState = this.getPlacementState(pos, placer);
        if (blockState == null) {
            return false;
        }

        if (!this.context.world().setBlockState(pos, blockState, Block.NOTIFY_ALL_AND_REDRAW)) {
            return false;
        }

        this.placed(blockState, pos, placer);
        return true;
    }

    private void placed(BlockState blockState, BlockPos pos, @Nullable LivingEntity placer) {
        ServerWorld world = this.context.world();
        ItemStack stack = this.context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        blockState = this.placeFromNbt(blockState, pos, stack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null && placer instanceof PlayerEntity playerPlacer) {
            blockEntity.itematic$placedFromItemStack(world, playerPlacer, blockState, pos, stack);
        }

        BlockItemAccessor.copyComponentsToBlockEntity(world, pos, stack);
        blockState.getBlock().onPlaced(world, pos, blockState, placer, stack);
        if (placer instanceof ServerPlayerEntity playerPlacer) {
            Criteria.PLACED_BLOCK.trigger(playerPlacer, pos, stack);
        }

        stack.itematic$invokeEvent(ItemEvents.PLACED_BLOCK, this.context);
        BlockSoundGroup blockSoundGroup = blockState.getSoundGroup();
        world.playSound(placer, pos, this.placeSound(blockSoundGroup), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
        world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(placer, blockState));
    }

    private SoundEvent placeSound(BlockSoundGroup group) {
        if (this.placeSound != null) {
            return this.placeSound.value();
        }

        return group.getPlaceSound();
    }

    @Nullable
    private BlockState getPlacementState(BlockPos pos, @Nullable LivingEntity placer) {
        if (this.operatorOnly && placer instanceof PlayerEntity playerPlacer && !playerPlacer.isCreativeLevelTwoOp()) {
            return null;
        }

        BlockState state = this.block.placementState(this.placementContext);
        return this.canPlace(state, pos, placer) ? state : null;
    }

    private boolean canPlace(BlockState state, BlockPos pos, @Nullable LivingEntity placer) {
        if (state == null) {
            return false;
        }

        ShapeContext shapeContext = ShapeContexts.ofNullable(placer);
        ServerWorld world = this.context.world();
        return state.canPlaceAt(world, pos) &&
            world.canPlace(state, pos, shapeContext);
    }

    private BlockState placeFromNbt(BlockState state, BlockPos pos, ItemStack stack) {
        BlockStateComponent blockStateProperties = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        if (blockStateProperties.isEmpty()) {
            return state;
        }

        BlockState modifiedState = blockStateProperties.applyToState(state);
        if (modifiedState != state) {
            this.context.world().setBlockState(pos, modifiedState, Block.NOTIFY_LISTENERS);
        }

        return modifiedState;
    }
}
