package net.errorcraft.itematic.item.placement.fluid;

import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.fluid.FluidUtil;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FluidPlacer {
    private final ActionContext context;
    private final PositionTarget position;
    private final RegistryEntry<Fluid> fluid;
    private final RegistryEntry<SoundEvent> placeSound;
    private final boolean mayOffset;

    public FluidPlacer(ActionContext context, PositionTarget position, RegistryEntry<Fluid> fluid, RegistryEntry<SoundEvent> placeSound, boolean mayOffset) {
        this.context = context;
        this.position = position;
        this.fluid = fluid;
        this.placeSound = placeSound;
        this.mayOffset = mayOffset;
    }

    public boolean place() {
        BlockPos pos = FluidUtil.getPlacementPosition(this.context, this.position);
        if (pos == null) {
            return false;
        }

        if (!this.shouldOffset(pos)) {
            return this.tryPlaceFluid(pos);
        }

        Direction direction = this.context.get(ItematicContextParameters.SIDE);
        if (direction == null) {
            return false;
        }

        return this.tryPlaceFluid(pos.offset(direction));
    }

    private boolean shouldOffset(BlockPos pos) {
        if (!this.mayOffset) {
            return false;
        }

        if (!this.fluid.matchesKey(FluidKeys.WATER)) {
            return true;
        }

        World world = this.context.world();
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof FluidFillable fluidFillable)) {
            return true;
        }

        LivingEntity filler = this.context.get(LootContextParameters.THIS_ENTITY, LivingEntity.class);
        return !fluidFillable.canFillWithFluid(filler, world, pos, state, this.fluid.value());
    }

    private boolean tryPlaceFluid(BlockPos pos) {
        if (!(this.fluid.value() instanceof FlowableFluid flowableFluid)) {
            return false;
        }

        BlockState state = this.context.world().getBlockState(pos);
        if (this.tryEvaporate(pos)) {
            return true;
        }

        if (this.tryFillWater(state, pos, flowableFluid)) {
            return true;
        }

        return this.tryPlaceFluidBlock(pos, state, flowableFluid);
    }

    private boolean tryEvaporate(BlockPos pos) {
        if (!(context.world() instanceof ServerWorld world)) {
            return false;
        }

        if (!world.getDimension().ultrawarm()) {
            return false;
        }

        if (!this.fluid.isIn(FluidTags.WATER)) {
            return false;
        }

        world.playSound(
            this.context.get(LootContextParameters.THIS_ENTITY),
            pos,
            SoundEvents.BLOCK_FIRE_EXTINGUISH,
            SoundCategory.BLOCKS,
            0.5f,
            2.6f + (world.random.nextFloat() - world.random.nextFloat()) * 0.8f
        );
        
        for (int i = 0; i < 8; i++) {
            world.spawnParticles(
                ParticleTypes.LARGE_SMOKE,
                pos.getX() + Math.random(),
                pos.getY() + Math.random(),
                pos.getZ() + Math.random(),
                0,
                0.0d,
                0.0d,
                0.0d,
                0.0d
            );
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

        fluidFillable.tryFillWithFluid(this.context.world(), pos, blockState, flowableFluid.getStill(false));
        this.playPlaceSound(pos);
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean tryPlaceFluidBlock(BlockPos pos, BlockState state, Fluid fluid) {
        if (!state.canBucketPlace(fluid)) {
            return false;
        }

        World world = this.context.world();
        if (!state.isAir() && !state.isLiquid()) {
            world.breakBlock(pos, true);
        }

        if (world.setBlockState(pos, fluid.getDefaultState().getBlockState(), Block.NOTIFY_ALL_AND_REDRAW) || state.getFluidState().isStill()) {
            this.playPlaceSound(pos);
            return true;
        }

        return false;
    }

    private void playPlaceSound(BlockPos pos) {
        if (this.placeSound == null) {
            return;
        }

        World world = this.context.world();
        Entity possiblePlacer = this.context.get(LootContextParameters.THIS_ENTITY);
        world.playSound(possiblePlacer, pos, this.placeSound.value(), SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent(possiblePlacer, GameEvent.FLUID_PLACE, pos);
    }
}
