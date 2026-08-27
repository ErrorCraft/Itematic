package net.errorcraft.itematic.mixin.world.entity.ai.behavior;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.CelebrateVillagersSurvivedRaid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(CelebrateVillagersSurvivedRaid.class)
public class CelebrateVillagersSurvivedRaidExtender extends Behavior<Villager> {
    @Unique
    private static final ScopedValue<ServerLevel> LEVEL = ScopedValue.newInstance();

    public CelebrateVillagersSurvivedRaidExtender(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState) {
        super(requiredMemoryState);
    }

    @WrapOperation(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/behavior/CelebrateVillagersSurvivedRaid;getFirework(Lnet/minecraft/world/item/DyeColor;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack passLevel(CelebrateVillagersSurvivedRaid instance, DyeColor color, int flightDuration, Operation<ItemStack> original, ServerLevel level) {
        return ScopedValue.where(LEVEL, level)
            .call(() -> original.call(instance, color, flightDuration));
    }

    @WrapOperation(
        method = "getFirework",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkRocketUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return LEVEL.get().itematic$createStack(ItemIds.FIREWORK_ROCKET);
    }
}
