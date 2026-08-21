package net.errorcraft.itematic.world.item.placement.fluid;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.errorcraft.itematic.world.level.material.FluidUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

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

        Level level = this.context.level();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BucketPickup fluidDrainable)) {
            return null;
        }

        Entity placer = this.context.get(LootContextParams.THIS_ENTITY);
        ItemStack drainedItemStack = fluidDrainable.pickupBlock(
            placer instanceof LivingEntity livingPlacer ? livingPlacer : null,
            level,
            pos,
            state
        );
        if (drainedItemStack.isEmpty()) {
            return null;
        }

        if (placer instanceof Player playerPlacer) {
            this.applyPlayerEffects(playerPlacer, fluidDrainable, drainedItemStack);
        }

        level.gameEvent(placer, GameEvent.FLUID_PICKUP, pos);
        return drainedItemStack;
    }

    private void applyPlayerEffects(Player player, BucketPickup fluidDrainable, ItemStack drainedItemStack) {
        ItemStack stack = this.context.get(LootContextParams.TOOL);
        if (!ItemStacks.isNullOrEmpty(stack)) {
            player.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
        }

        fluidDrainable.getPickupSound().ifPresent(sound -> player.playSound(sound, 1.0f, 1.0f));
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, drainedItemStack);
        }
    }
}
