package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record BlockItemComponent(Block block) implements ItemComponent {
    public static final Codec<BlockItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockItemComponent::block)
    ).apply(instance, BlockItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.BLOCK;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> useOnBlock(ItemUsageContext context) {
        return new TypedActionResult<>(this.place(new ItemPlacementContext(context)), context.getStack());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        this.block.appendTooltip(stack, world, tooltip, context);
    }

    private ActionResult place(ItemPlacementContext context) {
        if (!context.canPlace()) {
            return ActionResult.PASS;
        }
        BlockState blockState = this.getPlacementState(context);
        if (blockState == null) {
            return ActionResult.PASS;
        }
        if (!this.place(context, blockState)) {
            return ActionResult.PASS;
        }

        World world = context.getWorld();
        this.placed(blockState, context.getBlockPos(), world, context.getPlayer(), context.getStack());
        return ActionResult.success(world.isClient);
    }

    private void placed(BlockState blockState, BlockPos blockPos, World world, PlayerEntity player, ItemStack itemStack) {
        blockState = this.placeFromNbt(blockPos, world, itemStack, blockState);
        BlockItem.writeNbtToBlockEntity(world, player, blockPos, itemStack);
        blockState.getBlock().onPlaced(world, blockPos, blockState, player, itemStack);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.PLACED_BLOCK.trigger(serverPlayer, blockPos, itemStack);
        }

        BlockSoundGroup blockSoundGroup = blockState.getSoundGroup();
        world.playSound(player, blockPos, blockSoundGroup.getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
        world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(player, blockState));
        if (player == null || !player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
    }

    @Nullable
    private BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = this.block.getPlacementState(context);
        return blockState != null && this.canPlace(context, blockState) ? blockState : null;
    }

    private boolean place(ItemPlacementContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
    }

    private BlockState placeFromNbt(BlockPos pos, World world, ItemStack stack, BlockState state) {
        BlockState blockState = state;
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null) {
            NbtCompound nbtCompound2 = nbtCompound.getCompound("BlockStateTag");
            StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();

            for (String key : nbtCompound2.getKeys()) {
                Property<?> property = stateManager.getProperty(key);
                if (property != null) {
                    String value = nbtCompound2.getString(key);
                    blockState = with(blockState, property, value);
                }
            }
        }

        if (blockState != state) {
            world.setBlockState(pos, blockState, 2);
        }

        return blockState;
    }

    private static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
        return property.parse(name).map((value) -> state.with(property, value)).orElse(state);
    }

    private boolean canPlace(ItemPlacementContext context, BlockState state) {
        PlayerEntity playerEntity = context.getPlayer();
        ShapeContext shapeContext = playerEntity == null ? ShapeContext.absent() : ShapeContext.of(playerEntity);
        return state.canPlaceAt(context.getWorld(), context.getBlockPos()) && context.getWorld().canPlace(state, context.getBlockPos(), shapeContext);
    }
}
