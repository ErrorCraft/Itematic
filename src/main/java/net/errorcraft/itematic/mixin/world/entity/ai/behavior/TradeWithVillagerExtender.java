package net.errorcraft.itematic.mixin.world.entity.ai.behavior;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.npc.villager.Villagers;
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
import org.jspecify.annotations.Nullable;
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
public class TradeWithVillagerExtender {
    @Unique
    @Nullable
    private Map<Item, Integer> foodPointsCache;

    @WrapOperation(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;FOOD_POINTS:Ljava/util/Map;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Map<Item, Integer> getFoodPointsUseId(Operation<Map<Item, Integer>> original, ServerLevel level) {
        if (this.foodPointsCache == null) {
            this.foodPointsCache = Villagers.ITEM_FOOD_POINTS.entrySet()
                .stream()
                .collect(Collectors.toMap(
                    key -> level.itematic$getItem(key.getKey()).value(),
                    Map.Entry::getValue
                ));
        }

        return this.foodPointsCache;
    }

    @Redirect(
        method = "figureOutWhatIAmWillingToTrade",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/VillagerProfession;requestedItems()Lcom/google/common/collect/ImmutableSet;"
        )
    )
    private static ImmutableSet<Item> requestedItemsUseDynamicRegistry(VillagerProfession instance, Villager myBody) {
        TagKey<Item> gatherableItems = instance.itematic$gatherableItems();
        if (gatherableItems == null) {
            return ImmutableSet.of();
        }

        return myBody.registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .get(gatherableItems)
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
    private Item getWheatUseDynamicRegistry(ServerLevel level) {
        return level.itematic$getItem(ItemIds.WHEAT).value();
    }

    @Inject(
        method = "throwHalfStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private static void storeInventoryStackHolder(Villager villager, Set<Item> items, LivingEntity target, CallbackInfo info, @Local(name = "itemStack") ItemStack itemStack, @Share("foundItem") LocalRef<@Nullable Holder<Item>> foundItemReference) {
        foundItemReference.set(itemStack.typeHolder());
    }

    @Redirect(
        method = "throwHalfStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackUseHolder(ItemLike item, int count, @Share("foundItem") LocalRef<@Nullable Holder<Item>> foundItemReference) {
        Holder<Item> foundItem = foundItemReference.get();
        if (foundItem == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(foundItem, count);
    }
}
