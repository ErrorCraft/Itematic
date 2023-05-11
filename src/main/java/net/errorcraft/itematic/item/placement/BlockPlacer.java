package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.block.BlockStateUtil;
import net.errorcraft.itematic.block.ShapeContextUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BlockPlacer extends Placer {
    private final RegistryEntry<Block> block;
    private final ItemPlacementContext context;

    public BlockPlacer(ItemStack stack, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, RegistryEntry<Block> block, ItemPlacementContext context) {
        super(stack, world, blockPos, blockState, player);
        this.block = block;
        this.context = context;
    }

    public static BlockPlacer of(ItemUsageContext context, RegistryEntry<Block> block) {
        ItemPlacementContext placementContext = new ItemPlacementContext(context);
        World world = placementContext.getWorld();
        BlockPos blockPos = placementContext.getBlockPos();
        return new BlockPlacer(placementContext.getStack(), world, blockPos, world.getBlockState(blockPos), placementContext.getPlayer(), block, placementContext);
    }

    @Override
    public TypedActionResult<ItemStack> place() {
        if (!this.context.canPlace()) {
            return TypedActionResult.pass(this.stack);
        }
        BlockState blockState = this.getPlacementState();
        if (blockState == null) {
            return TypedActionResult.pass(this.stack);
        }
        if (!this.world.setBlockState(this.blockPos, blockState, Block.field_31022)) {
            return TypedActionResult.pass(this.stack);
        }

        this.placed(blockState);
        return TypedActionResult.success(this.stack, this.world.isClient());
    }

    private void placed(BlockState blockState) {
        blockState = this.placeFromNbt(blockState);
        BlockItem.writeNbtToBlockEntity(this.world, this.player, this.blockPos, this.stack);
        blockState.getBlock().onPlaced(this.world, this.blockPos, blockState, this.player, this.stack);
        if (this.player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.PLACED_BLOCK.trigger(serverPlayer, this.blockPos, this.stack);
        }

        BlockSoundGroup blockSoundGroup = blockState.getSoundGroup();
        this.world.playSound(this.player, this.blockPos, blockSoundGroup.getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
        this.world.emitGameEvent(GameEvent.BLOCK_PLACE, this.blockPos, GameEvent.Emitter.of(this.player, blockState));
        if (this.player == null || !this.player.getAbilities().creativeMode) {
            this.stack.decrement(1);
        }
    }

    @Nullable
    private BlockState getPlacementState() {
        BlockState blockState = this.block.value().getPlacementState(this.context);
        return this.canPlace(blockState) ? blockState : null;
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
        NbtCompound nbtCompound = this.stack.getNbt();
        if (nbtCompound != null) {
            NbtCompound nbtCompound2 = nbtCompound.getCompound(BlockItem.BLOCK_STATE_TAG_KEY);
            StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();

            for (String key : nbtCompound2.getKeys()) {
                Property<?> property = stateManager.getProperty(key);
                if (property != null) {
                    String value = nbtCompound2.getString(key);
                    blockState = BlockStateUtil.with(blockState, property, value);
                }
            }
        }

        if (blockState != state) {
            this.world.setBlockState(this.blockPos, blockState, Block.NOTIFY_LISTENERS);
        }

        return blockState;
    }
}
