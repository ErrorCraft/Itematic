package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.block.AbstractBlockAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.errorcraft.itematic.item.UnplaceableItemPlacementContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ScaffoldingBlock.class)
public class ScaffoldingBlockExtender extends Block implements AbstractBlockAccess {
    @Shadow
    @Final
    public static int STABILITY_MAX_DISTANCE;

    public ScaffoldingBlockExtender(Properties settings) {
        super(settings);
    }

    @ModifyArg(
        method = "getShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/shapes/CollisionContext;isHoldingItem(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private Item getLightUseDynamicRegistry(Item item, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockGetter world) {
        if (world instanceof ItemAccess itemAccess) {
            return itemAccess.getOptionalEntry(state.getBlock().itematic$asItemKey())
                .map(Holder::value)
                .orElse(null);
        }

        return null;
    }

    @Override
    public BlockPlaceContext itematic$placementContext(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (world.getBlockState(pos).is(this)) {
            return this.scaffoldingPlacementContext(context, world, pos);
        }

        if (ScaffoldingBlock.getDistance(world, pos) == STABILITY_MAX_DISTANCE) {
            return UnplaceableItemPlacementContext.of(context);
        }

        return context;
    }

    @Unique
    private BlockPlaceContext scaffoldingPlacementContext(BlockPlaceContext context, Level world, BlockPos blockPos) {
        Direction direction = this.scaffoldingDirection(context);
        BlockPos.MutableBlockPos mutable = blockPos.mutable().move(direction);
        int distance = 0;
        while (distance < STABILITY_MAX_DISTANCE) {
            if (this.tooHigh(context, world, mutable)) {
                break;
            }

            BlockState state = world.getBlockState(mutable);
            if (!state.is(this)) {
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
    private Direction scaffoldingDirection(BlockPlaceContext context) {
        if (context.isSecondaryUseActive()) {
            return context.isInside() ? context.getClickedFace().getOpposite() : context.getClickedFace();
        }

        if (context.getClickedFace() == Direction.UP) {
            return context.getHorizontalDirection();
        }

        return Direction.UP;
    }

    @Unique
    private boolean tooHigh(BlockPlaceContext context, Level world, BlockPos pos) {
        if (world.isClientSide() || world.isInWorldBounds(pos)) {
            return false;
        }

        int topY = world.getMaxY();
        if (context.getPlayer() instanceof ServerPlayer player && pos.getY() >= topY) {
            player.sendSystemMessage(Component.translatable("build.tooHigh", topY - 1).withStyle(ChatFormatting.RED), true);
        }

        return true;
    }

    @Unique
    private BlockPlaceContext offsetPlacementContext(BlockPlaceContext context, BlockState state, BlockPos pos, Direction direction) {
        if (state.canBeReplaced(context)) {
            return BlockPlaceContext.at(context, pos, direction);
        }

        return UnplaceableItemPlacementContext.of(context);
    }
}
