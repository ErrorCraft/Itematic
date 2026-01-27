package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.access.block.entity.BlockEntityAccess;
import net.errorcraft.itematic.block.BlockStateUtil;
import net.errorcraft.itematic.block.ShapeContextUtil;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.mixin.block.BlockItemAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockPlacer extends Placer {
    private final BlockPicker<?> block;
    private final ItemPlacementContext context;
    private final boolean operatorOnly;
    private final boolean decrementStack;

    public BlockPlacer(ItemStack stack, ItemStackConsumer resultStackConsumer, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, BlockPicker<?> block, ItemPlacementContext context, boolean operatorOnly, boolean decrementStack) {
        super(stack, resultStackConsumer, world, blockPos, blockState, player);
        this.block = block;
        this.context = context;
        this.operatorOnly = operatorOnly;
        this.decrementStack = decrementStack;
    }

    public static BlockPlacer of(ActionContext context, ActionContextParameter position, BlockPicker<?> block, boolean operatorOnly, boolean decrementStack) {
        return of(context.createItemPlacementContext(position, block.defaultBlock()), context.resultStackConsumer(), block, operatorOnly, decrementStack);
    }

    public static BlockPlacer of(ItemUsageContext context, ItemStackConsumer resultStackConsumer, BlockPicker<?> block, boolean operatorOnly, boolean decrementStack) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Direction side = context.getSide();
        ItemPlacementContext placementContext = block.placementContext(context.getPlayer() != null ? new ItemPlacementContext(context) : new AutomaticItemPlacementContext(world, pos, side, context.getStack(), world.isAir(pos.down()) ? side : Direction.UP));
        return of(placementContext, resultStackConsumer, block, operatorOnly, decrementStack);
    }

    public static BlockPlacer of(ItemPlacementContext context, ItemStackConsumer resultStackConsumer, BlockPicker<?> block, boolean operatorOnly, boolean decrementStack) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        return new BlockPlacer(context.getStack(), resultStackConsumer, world, blockPos, world.getBlockState(blockPos), context.getPlayer(), block, context, operatorOnly, decrementStack);
    }

    @Override
    public ItemResult place() {
        if (!this.context.canPlace()) {
            return ItemResult.PASS;
        }
        BlockState blockState = this.getPlacementState();
        if (blockState == null) {
            return ItemResult.PASS;
        }
        if (!this.world.setBlockState(this.blockPos, blockState, Block.NOTIFY_ALL_AND_REDRAW)) {
            return ItemResult.PASS;
        }

        this.placed(blockState);
        return ItemResult.SUCCESS;
    }

    private void placed(BlockState blockState) {
        blockState = this.placeFromNbt(blockState);
        BlockEntity blockEntity = this.world.getBlockEntity(this.blockPos);
        if (blockEntity != null) {
            ((BlockEntityAccess) blockEntity).itematic$placedFromItemStack(this.world, this.player, blockState, this.blockPos, this.stack);
        }
        BlockItemAccessor.copyComponentsToBlockEntity(this.world, this.blockPos, this.stack);
        blockState.getBlock().onPlaced(this.world, this.blockPos, blockState, this.player, this.stack);
        if (this.player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.PLACED_BLOCK.trigger(serverPlayer, this.blockPos, this.stack);
            ActionContext context = ActionContext.builder(serverPlayer.getServerWorld(), this.stack, this.resultStackConsumer, this.context.getHand())
                .entityPosition(ActionContextParameter.THIS, serverPlayer)
                .position(ActionContextParameter.TARGET, this.blockPos)
                .build();
            this.stack.itematic$invokeEvent(ItemEvents.PLACED_BLOCK, context);
        }

        BlockSoundGroup blockSoundGroup = blockState.getSoundGroup();
        this.world.playSound(this.player, this.blockPos, blockSoundGroup.getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
        this.world.emitGameEvent(GameEvent.BLOCK_PLACE, this.blockPos, GameEvent.Emitter.of(this.player, blockState));
        if (this.decrementStack) {
            this.tryDecrementStack();
        }
    }

    @Nullable
    private BlockState getPlacementState() {
        if (this.operatorOnly && this.player != null && !this.player.isCreativeLevelTwoOp()) {
            return null;
        }
        BlockState state = this.block.placementState(this.context);
        return this.canPlace(state) ? state : null;
    }

    private boolean canPlace(BlockState state) {
        if (state == null) {
            return false;
        }
        ShapeContext shapeContext = ShapeContextUtil.ofNullable(this.player);
        return state.canPlaceAt(this.world, this.blockPos) && this.world.canPlace(state, this.blockPos, shapeContext);
    }

    private BlockState placeFromNbt(BlockState state) {
        BlockState blockState = state;
        BlockStateComponent blockStates = this.stack.get(DataComponentTypes.BLOCK_STATE);
        if (blockStates != null) {
            StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();

            for (Map.Entry<String, String> entry : blockStates.properties().entrySet()) {
                Property<?> property = stateManager.getProperty(entry.getKey());
                if (property != null) {
                    blockState = BlockStateUtil.with(blockState, property, entry.getValue());
                }
            }
        }

        if (blockState != state) {
            this.world.setBlockState(this.blockPos, blockState, Block.NOTIFY_LISTENERS);
        }

        return blockState;
    }
}
