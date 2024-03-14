package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.block.BlockAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.errorcraft.itematic.item.UnplaceableItemPlacementContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ScaffoldingBlock.class)
public class ScaffoldingBlockExtender extends Block implements BlockAccess {
    @Shadow
    @Final
    public static int MAX_DISTANCE;

    public ScaffoldingBlockExtender(Settings settings) {
        super(settings);
    }

    @ModifyArg(
        method = "getOutlineShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/ShapeContext;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private Item getLightUseDynamicRegistry(Item item, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockView world) {
        if (world instanceof ItemAccess itemAccess) {
            return itemAccess.getOptionalEntry(state.getBlock().itematic$asItemKey())
                .map(RegistryEntry::value)
                .orElse(null);
        }
        return null;
    }

    @Override
    public ItemPlacementContext itematic$placementContext(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        if (world.getBlockState(pos).isOf(this)) {
            return this.scaffoldingPlacementContext(context, world, pos);
        }
        if (ScaffoldingBlock.calculateDistance(world, pos) == MAX_DISTANCE) {
            return UnplaceableItemPlacementContext.of(context);
        }
        return context;
    }

    @Unique
    private ItemPlacementContext scaffoldingPlacementContext(ItemPlacementContext context, World world, BlockPos blockPos) {
        Direction direction = this.scaffoldingDirection(context);
        BlockPos.Mutable mutable = blockPos.mutableCopy().move(direction);
        int distance = 0;
        while (distance < MAX_DISTANCE) {
            if (this.tooHigh(context, world, mutable)) {
                break;
            }
            BlockState state = world.getBlockState(mutable);
            if (!state.isOf(this)) {
                return this.offsetPlacementContext(context, state, mutable, direction);
            }
            mutable.move(direction);
            if (direction.getAxis().isHorizontal()) {
                distance++;
            }
        }
        return UnplaceableItemPlacementContext.of(context);
    }

    @Unique
    private Direction scaffoldingDirection(ItemPlacementContext context) {
        if (context.shouldCancelInteraction()) {
            return context.hitsInsideBlock() ? context.getSide().getOpposite() : context.getSide();
        }
        if (context.getSide() == Direction.UP) {
            return context.getHorizontalPlayerFacing();
        }
        return Direction.UP;
    }

    @Unique
    private boolean tooHigh(ItemPlacementContext context, World world, BlockPos pos) {
        if (world.isClient() || world.isInBuildLimit(pos)) {
            return false;
        }
        int topY = world.getTopY();
        if (context.getPlayer() instanceof ServerPlayerEntity player && pos.getY() >= topY) {
            player.sendMessageToClient(Text.translatable("build.tooHigh", topY - 1).formatted(Formatting.RED), true);
        }
        return true;
    }

    @Unique
    private ItemPlacementContext offsetPlacementContext(ItemPlacementContext context, BlockState state, BlockPos pos, Direction direction) {
        if (state.canReplace(context)) {
            return ItemPlacementContext.offset(context, pos, direction);
        }
        return UnplaceableItemPlacementContext.of(context);
    }
}
