package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.entity.passive.VillagerEntityUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.GatherItemsVillagerTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerProfession;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(GatherItemsVillagerTask.class)
public class GatherItemsVillagerTaskExtender {
    @Unique
    private Map<Item, Integer> itemFoodPointsCache;

    @Redirect(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/passive/VillagerEntity;ITEM_FOOD_VALUES:Ljava/util/Map;",
            opcode = Opcodes.GETSTATIC
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
        method = "getGatherableItems",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/VillagerProfession;gatherableItems()Lcom/google/common/collect/ImmutableSet;"
        )
    )
    private static ImmutableSet<Item> gatherableItemsUseDynamicRegistry(VillagerProfession instance, VillagerEntity entity) {
        TagKey<Item> tag = instance.itematic$gatherableItems();
        if (tag == null) {
            return ImmutableSet.of();
        }

        return entity.getRegistryManager()
            .getOrThrow(RegistryKeys.ITEM)
            .getOptional(tag)
            .stream()
            .flatMap(RegistryEntryList::stream)
            .map(RegistryEntry::value)
            .collect(ImmutableSet.toImmutableSet());
    }

    @Redirect(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;WHEAT:Lnet/minecraft/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item keepRunningGetWheatUseDynamicRegistry(ServerWorld serverWorld) {
        return serverWorld.itematic$getItem(ItemKeys.WHEAT).value();
    }

    @Inject(
        method = "giveHalfOfStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private static void storeInventoryStackRegistryEntry(VillagerEntity villager, Set<Item> validItems, LivingEntity target, CallbackInfo info, @Local(ordinal = 1) ItemStack inventoryStack, @Share("registryEntry") LocalRef<RegistryEntry<Item>> foundItem) {
        foundItem.set(inventoryStack.getRegistryEntry());
    }

    @Redirect(
        method = "giveHalfOfStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackUseRegistryEntry(ItemConvertible item, int count, @Share("registryEntry") LocalRef<RegistryEntry<Item>> foundItem) {
        if (foundItem.get() == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(foundItem.get(), count);
    }
}
