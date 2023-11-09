package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.brain.task.GatherItemsVillagerTask;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GatherItemsVillagerTask.class)
public class GatherItemsVillagerTaskExtender {
    @Redirect(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;WHEAT:Lnet/minecraft/item/Item;"
        )
    )
    private Item keepRunningGetWheatUseDynamicRegistry(ServerWorld serverWorld) {
        return serverWorld.itematic$getItem(ItemKeys.WHEAT).value();
    }
}
