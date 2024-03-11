package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CelebrateRaidWinTask;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CelebrateRaidWinTask.class)
public class CelebrateRaidWinTaskExtender extends MultiTickTask<VillagerEntity> {
    @Unique
    private ServerWorld world;

    public CelebrateRaidWinTaskExtender(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
        super(requiredMemoryState);
    }
    
    @Inject(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ai/brain/task/CelebrateRaidWinTask;createFirework(Lnet/minecraft/util/DyeColor;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private void keepRunningStoreServerWorld(ServerWorld serverWorld, VillagerEntity villagerEntity, long l, CallbackInfo info) {
        this.world = serverWorld;
    }

    @Inject(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/entity/ai/brain/task/CelebrateRaidWinTask;createFirework(Lnet/minecraft/util/DyeColor;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private void keepRunningResetServerWorld(ServerWorld serverWorld, VillagerEntity villagerEntity, long l, CallbackInfo info) {
        this.world = null;
    }

    @Redirect(
        method = "createFirework",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkRocketUseCreateStack(ItemConvertible item) {
        return this.world.itematic$createStack(ItemKeys.FIREWORK_ROCKET);
    }

    @Redirect(
        method = "createFirework",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkStarUseCreateStack(ItemConvertible item) {
        return this.world.itematic$createStack(ItemKeys.FIREWORK_STAR);
    }
}
