package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.world.level.block.state.BlockBehaviourAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.context.UnplaceableBlockPlaceContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScaffoldingBlock.class)
public class ScaffoldingBlockExtender extends Block implements BlockBehaviourAccess {
    @Shadow
    @Final
    public static int STABILITY_MAX_DISTANCE;

    public ScaffoldingBlockExtender(Properties settings) {
        super(settings);
    }

    @WrapOperation(
        method = "getShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/shapes/CollisionContext;isHoldingItem(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingScaffoldingCheckId(CollisionContext instance, Item item, Operation<Boolean> original) {
        return instance.itematic$isHoldingItem(ItemIds.SCAFFOLDING);
    }

    @Override
    public BlockPlaceContext itematic$blockPlaceContext(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.getBlockState(pos).is(this)) {
            return this.scaffoldingBlockPlaceContext(context, level, pos);
        }

        if (ScaffoldingBlock.getDistance(level, pos) == STABILITY_MAX_DISTANCE) {
            return UnplaceableBlockPlaceContext.of(context);
        }

        return context;
    }

    @Unique
    private BlockPlaceContext scaffoldingBlockPlaceContext(BlockPlaceContext context, Level level, BlockPos blockPos) {
        Direction direction = this.scaffoldingDirection(context);
        BlockPos.MutableBlockPos mutable = blockPos.mutable().move(direction);
        int distance = 0;
        while (distance < STABILITY_MAX_DISTANCE) {
            if (this.tooHigh(context, level, mutable)) {
                break;
            }

            BlockState state = level.getBlockState(mutable);
            if (!state.is(this)) {
                return this.offsetBlockPlaceContext(context, state, mutable, direction);
            }

            mutable.move(direction);
            if (direction.getAxis().isHorizontal()) {
                distance++;
            }
        }

        return UnplaceableBlockPlaceContext.of(context);
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
    private boolean tooHigh(BlockPlaceContext context, Level level, BlockPos pos) {
        if (level.isClientSide() || level.isInWorldBounds(pos)) {
            return false;
        }

        int topY = level.getMaxY();
        if (context.getPlayer() instanceof ServerPlayer player && pos.getY() >= topY) {
            player.sendSystemMessage(Component.translatable("build.tooHigh", topY - 1).withStyle(ChatFormatting.RED), true);
        }

        return true;
    }

    @Unique
    private BlockPlaceContext offsetBlockPlaceContext(BlockPlaceContext context, BlockState state, BlockPos pos, Direction direction) {
        if (state.canBeReplaced(context)) {
            return BlockPlaceContext.at(context, pos, direction);
        }

        return UnplaceableBlockPlaceContext.of(context);
    }
}
