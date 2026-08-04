package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.entity.passive.VillagerEntityUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.TradeWithVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
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

@Mixin(TradeWithVillager.class)
public class GatherItemsVillagerTaskExtender {
    @Unique
    private Map<Item, Integer> itemFoodPointsCache;

    @Redirect(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;FOOD_POINTS:Ljava/util/Map;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Map<Item, Integer> getItemFoodPointsUseRegistryKey(ServerLevel serverWorld) {
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
        method = "figureOutWhatIAmWillingToTrade",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/VillagerProfession;requestedItems()Lcom/google/common/collect/ImmutableSet;"
        )
    )
    private static ImmutableSet<Item> gatherableItemsUseDynamicRegistry(VillagerProfession instance, Villager entity) {
        TagKey<Item> tag = instance.itematic$gatherableItems();
        if (tag == null) {
            return ImmutableSet.of();
        }

        return entity.registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .get(tag)
            .stream()
            .flatMap(HolderSet::stream)
            .map(Holder::value)
            .collect(ImmutableSet.toImmutableSet());
    }

    @Redirect(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;WHEAT:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item keepRunningGetWheatUseDynamicRegistry(ServerLevel serverWorld) {
        return serverWorld.itematic$getItem(ItemKeys.WHEAT).value();
    }

    @Inject(
        method = "throwHalfStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private static void storeInventoryStackRegistryEntry(Villager villager, Set<Item> validItems, LivingEntity target, CallbackInfo info, @Local(ordinal = 1) ItemStack inventoryStack, @Share("registryEntry") LocalRef<Holder<Item>> foundItem) {
        foundItem.set(inventoryStack.getItemHolder());
    }

    @Redirect(
        method = "throwHalfStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackUseRegistryEntry(ItemLike item, int count, @Share("registryEntry") LocalRef<Holder<Item>> foundItem) {
        if (foundItem.get() == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(foundItem.get(), count);
    }
}
