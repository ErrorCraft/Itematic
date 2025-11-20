package net.errorcraft.itematic.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerExtender {
    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Redirect(
        method = "tryBreakBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;canMine(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)Z"
        )
    )
    private boolean tryBreakBlockCanMineUseItemStackVersion(Item instance, BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return this.player.getMainHandStack().itematic$canMine(state, world, pos, miner);
    }

    @Redirect(
        method = "interactItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getMaxUseTime()I"
        )
    )
    private int getMaxUseTimeUseCustomImplementation(ItemStack instance) {
        return instance.itematic$useDuration(this.player);
    }
}
