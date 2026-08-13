package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.CelebrateVillagersSurvivedRaid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CelebrateVillagersSurvivedRaid.class)
public class CelebrateRaidWinTaskExtender extends Behavior<Villager> {
    @Unique
    private ServerLevel world;

    public CelebrateRaidWinTaskExtender(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState) {
        super(requiredMemoryState);
    }
    
    @Inject(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/behavior/CelebrateVillagersSurvivedRaid;getFirework(Lnet/minecraft/world/item/DyeColor;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void storeServerWorld(ServerLevel serverWorld, Villager villagerEntity, long l, CallbackInfo info) {
        this.world = serverWorld;
    }

    @Inject(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/entity/ai/behavior/CelebrateVillagersSurvivedRaid;getFirework(Lnet/minecraft/world/item/DyeColor;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void resetServerWorld(ServerLevel serverWorld, Villager villagerEntity, long l, CallbackInfo info) {
        this.world = null;
    }

    @Redirect(
        method = "getFirework",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkRocketUseCreateStack(ItemLike item) {
        return this.world.itematic$createStack(ItemIds.FIREWORK_ROCKET);
    }
}
