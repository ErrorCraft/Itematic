package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.entity.passive.VillagerEntityUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.brain.task.GatherItemsVillagerTask;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(GatherItemsVillagerTask.class)
public class GatherItemsVillagerTaskExtender {
    @Unique
    private Map<Item, Integer> itemFoodPointsCache;

    @Redirect(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/passive/VillagerEntity;ITEM_FOOD_VALUES:Ljava/util/Map;"
        )
    )
    private Map<Item, Integer> getItemFoodPointsUseRegistryKey(ServerWorld serverWorld) {
        if (this.itemFoodPointsCache == null) {
            this.itemFoodPointsCache = VillagerEntityUtil.ITEM_FOOD_POINTS.entrySet()
                .stream()
                .collect(Collectors.toMap(
                    key -> serverWorld.itematic$getItem(key.getKey()).value(),
                    Map.Entry::getValue
                ));
        }
        return this.itemFoodPointsCache;
    }

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
