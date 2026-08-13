package net.errorcraft.itematic.world.item.placement.fluid;

import net.errorcraft.itematic.references.FluidIds;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.level.material.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class FluidPlacer {
    private final ActionContext context;
    private final PositionTarget position;
    private final Holder<Fluid> fluid;
    private final Holder<SoundEvent> placeSound;
    private final boolean mayOffset;

    public FluidPlacer(ActionContext context, PositionTarget position, Holder<Fluid> fluid, Holder<SoundEvent> placeSound, boolean mayOffset) {
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

        return this.tryPlaceFluid(pos.relative(direction));
    }

    private boolean shouldOffset(BlockPos pos) {
        if (!this.mayOffset) {
            return false;
        }

        if (!this.fluid.is(FluidIds.WATER)) {
            return true;
        }

        Level world = this.context.world();
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof LiquidBlockContainer fluidFillable)) {
            return true;
        }

        LivingEntity filler = this.context.get(LootContextParams.THIS_ENTITY, LivingEntity.class);
        return !fluidFillable.canPlaceLiquid(filler, world, pos, state, this.fluid.value());
    }

    private boolean tryPlaceFluid(BlockPos pos) {
        if (!(this.fluid.value() instanceof FlowingFluid flowableFluid)) {
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
        Level world = this.context.world();
        if (!world.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)) {
            return false;
        }

        if (!this.fluid.is(FluidTags.WATER)) {
            return false;
        }

        world.playSound(
            this.context.get(LootContextParams.THIS_ENTITY),
            pos,
            SoundEvents.FIRE_EXTINGUISH,
            SoundSource.BLOCKS,
            0.5f,
            2.6f + (world.random.nextFloat() - world.random.nextFloat()) * 0.8f
        );
        for (int i = 0; i < 8; i++) {
            world.addParticle(
                ParticleTypes.LARGE_SMOKE,
                pos.getX() + Math.random(),
                pos.getY() + Math.random(),
                pos.getZ() + Math.random(),
                0.0d,
                0.0d,
                0.0d
            );
        }

        return true;
    }

    private boolean tryFillWater(BlockState blockState, BlockPos pos, FlowingFluid flowableFluid) {
        if (!(blockState.getBlock() instanceof LiquidBlockContainer fluidFillable)) {
            return false;
        }

        if (!this.fluid.is(FluidIds.WATER)) {
            return false;
        }

        fluidFillable.placeLiquid(this.context.world(), pos, blockState, flowableFluid.getSource(false));
        this.playPlaceSound(pos);
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean tryPlaceFluidBlock(BlockPos pos, BlockState state, Fluid fluid) {
        if (!state.canBeReplaced(fluid)) {
            return false;
        }

        Level world = this.context.world();
        if (!state.isAir() && !state.liquid()) {
            world.destroyBlock(pos, true);
        }

        if (world.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), Block.UPDATE_ALL_IMMEDIATE) || state.getFluidState().isSource()) {
            this.playPlaceSound(pos);
            return true;
        }

        return false;
    }

    private void playPlaceSound(BlockPos pos) {
        if (this.placeSound == null) {
            return;
        }

        Level world = this.context.world();
        Entity possiblePlacer = this.context.get(LootContextParams.THIS_ENTITY);
        world.playSound(possiblePlacer, pos, this.placeSound.value(), SoundSource.BLOCKS, 1.0f, 1.0f);
        world.gameEvent(possiblePlacer, GameEvent.FLUID_PLACE, pos);
    }
}
