package net.errorcraft.itematic.item.placement.fluid;

import net.errorcraft.itematic.fluid.FluidUtil;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class FluidDrainer {
    private final ActionContext context;
    private final PositionTarget position;

    public FluidDrainer(ActionContext context, PositionTarget position) {
        this.context = context;
        this.position = position;
    }

    @Nullable
    public ItemStack drain() {
        BlockPos pos = FluidUtil.getPlacementPosition(this.context, this.position);
        if (pos == null) {
            return null;
        }

        World world = this.context.world();
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof FluidDrainable fluidDrainable)) {
            return null;
        }

        Entity placer = this.context.get(LootContextParameters.THIS_ENTITY);
        ItemStack drainedItemStack = fluidDrainable.tryDrainFluid(
            placer instanceof LivingEntity livingPlacer ? livingPlacer : null,
            world,
            pos,
            state
        );
        if (drainedItemStack.isEmpty()) {
            return null;
        }

        if (placer instanceof PlayerEntity playerPlacer) {
            this.applyPlayerEffects(playerPlacer, fluidDrainable, drainedItemStack);
        }

        world.emitGameEvent(placer, GameEvent.FLUID_PICKUP, pos);
        return drainedItemStack;
    }

    private void applyPlayerEffects(PlayerEntity player, FluidDrainable fluidDrainable, ItemStack drainedItemStack) {
        ItemStack stack = this.context.get(LootContextParameters.TOOL);
        if (!ItemStackUtil.isNullOrEmpty(stack)) {
            player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        }

        fluidDrainable.getBucketFillSound().ifPresent(sound -> player.playSound(sound, 1.0f, 1.0f));
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.FILLED_BUCKET.trigger(serverPlayer, drainedItemStack);
        }
    }
}
