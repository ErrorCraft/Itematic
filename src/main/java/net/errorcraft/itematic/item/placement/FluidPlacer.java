package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FluidPlacer extends Placer {
    private final RegistryEntry<Fluid> fluid;
    private final RegistryEntry<SoundEvent> emptyingSound;
    private final Direction direction;
    private final boolean allowOffset;

    protected FluidPlacer(ItemStack stack, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, RegistryEntry<Fluid> fluid, RegistryEntry<SoundEvent> emptyingSound, Direction direction, boolean allowOffset) {
        super(stack, world, blockPos, blockState, player);
        this.fluid = fluid;
        this.emptyingSound = emptyingSound;
        this.direction = direction;
        this.allowOffset = allowOffset;
    }

    public static FluidPlacer of(ItemStack stack, World world, BlockHitResult hitResult, PlayerEntity player, RegistryEntry<Fluid> fluid, RegistryEntry<SoundEvent> emptyingSound) {
        BlockPos blockPos = hitResult.getBlockPos();
        return new FluidPlacer(stack, world, blockPos, world.getBlockState(blockPos), player, fluid, emptyingSound, hitResult.getSide(), hitResult.isInsideBlock());
    }

    @Override
    public TypedActionResult<ItemStack> place() {
        BlockPos offset = this.blockPos.offset(this.direction);
        if (this.player != null && (!this.world.canPlayerModifyAt(this.player, this.blockPos) || !this.player.canPlaceOn(offset, this.direction, this.stack))) {
            return TypedActionResult.pass(this.stack);
        }
        if (this.fluid.matchesKey(FluidKeys.EMPTY)) {
            return this.tryDrainFluid();
        }
        BlockPos actualBlockPos = this.getActualPosition(offset);
        if (!this.tryPlaceFluid(actualBlockPos, this.allowOffset)) {
            return TypedActionResult.pass(this.stack);
        }
        ItemStack resultStack = new ItemStack(this.world.getItem(ItemKeys.BUCKET));
        return TypedActionResult.success(resultStack, this.world.isClient());
    }

    private BlockPos getActualPosition(BlockPos offset) {
        if (!this.allowOffset) {
            return this.blockPos;
        }
        if (this.blockState.getBlock() instanceof FluidFillable && this.fluid.matchesKey(FluidKeys.WATER)) {
            return this.blockPos;
        }
        return offset;
    }

    private TypedActionResult<ItemStack> tryDrainFluid() {
        if (!(this.blockState.getBlock() instanceof FluidDrainable fluidDrainable)) {
            return TypedActionResult.pass(this.stack);
        }
        ItemStack drainedItemStack = fluidDrainable.tryDrainFluid(this.world, this.blockPos, this.blockState);
        if (drainedItemStack.isEmpty()) {
            return TypedActionResult.pass(this.stack);
        }
        this.applyPlayerEffects(fluidDrainable, drainedItemStack);
        this.world.emitGameEvent(this.player, GameEvent.FLUID_PICKUP, this.blockPos);
        return TypedActionResult.success(drainedItemStack, this.world.isClient());
    }

    private void applyPlayerEffects(FluidDrainable fluidDrainable, ItemStack drainedItemStack) {
        if (this.player == null) {
            return;
        }

        this.player.incrementStat(Stats.USED.getOrCreateStat(this.stack.getItem()));
        fluidDrainable.getBucketFillSound().ifPresent(sound -> this.player.playSound(sound, 1.0f, 1.0f));
        if (this.player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.FILLED_BUCKET.trigger(serverPlayer, drainedItemStack);
        }
    }

    @SuppressWarnings("deprecation")
    private boolean tryPlaceFluid(BlockPos pos, boolean allowOffset) {
        Fluid fluid = this.fluid.value();
        if (!(fluid instanceof FlowableFluid flowableFluid)) {
            return false;
        }
        BlockState blockState = this.world.getBlockState(pos);
        boolean canPlace = blockState.canBucketPlace(fluid);
        if (!blockState.isAir() && !canPlace && !(blockState.getBlock() instanceof FluidFillable fluidFillable && fluidFillable.canFillWithFluid(this.world, pos, blockState, fluid))) {
            return allowOffset && this.tryPlaceFluid(this.blockPos.offset(this.direction), false);
        }
        if (this.tryEvaporate(pos)) {
            return true;
        }
        if (this.tryFillWater(blockState, pos, flowableFluid)) {
            return true;
        }
        if (!this.world.isClient() && canPlace && !blockState.isLiquid()) {
            this.world.breakBlock(pos, true);
        }
        if (this.world.setBlockState(pos, fluid.getDefaultState().getBlockState(), Block.field_31022) || blockState.getFluidState().isStill()) {
            this.playEmptyingSound(pos);
            return true;
        }
        return false;
    }

    private boolean tryEvaporate(BlockPos pos) {
        if (!this.world.getDimension().ultrawarm()) {
            return false;
        }
        if (!this.fluid.isIn(FluidTags.WATER)) {
            return false;
        }
        this.world.playSound(this.player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.8f);
        for (int i = 0; i < 8; ++i) {
            this.world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0d, 0.0d, 0.0d);
        }
        return true;
    }

    private boolean tryFillWater(BlockState blockState, BlockPos pos, FlowableFluid flowableFluid) {
        if (!(blockState.getBlock() instanceof FluidFillable fluidFillable)) {
            return false;
        }
        if (!this.fluid.matchesKey(FluidKeys.WATER)) {
            return false;
        }
        fluidFillable.tryFillWithFluid(this.world, pos, blockState, flowableFluid.getStill(false));
        this.playEmptyingSound(pos);
        return true;
    }

    private void playEmptyingSound(BlockPos pos) {
        if (this.emptyingSound == null) {
            return;
        }
        this.world.playSound(this.player, pos, this.emptyingSound.value(), SoundCategory.BLOCKS, 1.0f, 1.0f);
        this.world.emitGameEvent(this.player, GameEvent.FLUID_PLACE, pos);
    }
}
