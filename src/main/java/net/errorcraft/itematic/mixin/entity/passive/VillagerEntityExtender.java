package net.errorcraft.itematic.mixin.entity.passive;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.world.entity.npc.villager.Villagers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(Villager.class)
public abstract class VillagerEntityExtender extends MerchantEntityExtender {
    @Shadow
    public abstract VillagerData getVillagerData();

    protected VillagerEntityExtender(EntityType<? extends AgeableMob> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "wantsToPickUp",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableSet;contains(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean containsForGatherableItemsUseItemTagCheck(ImmutableSet<Item> instance, Object o, @Local(argsOnly = true) ItemStack stack) {
        TagKey<Item> tag = this.getVillagerData().profession().value().itematic$gatherableItems();
        if (tag == null) {
            return false;
        }

        return stack.is(tag);
    }

    @Redirect(
        method = "increaseMerchantCareer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;updateTrades(Lnet/minecraft/server/level/ServerLevel;)V"
        )
    )
    private void fillRecipesUseDynamicRegistry(Villager instance, ServerLevel world) {
        this.fillRecipesFromContext(world);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForVillagerSpawnEggUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.VILLAGER_SPAWN_EGG);
    }

    @Redirect(
        method = "eatUntilFull",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <K, V> V getFoodPointsUseRegistryKey(Map<K, V> instance, Object o, @Local ItemStack stack) {
        return (V) Villagers.ITEM_FOOD_POINTS.get(stack.itematic$key());
    }

    @Redirect(
        method = "countFoodPointsInInventory",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;stream()Ljava/util/stream/Stream;"
        )
    )
    private Stream<Map.Entry<Item, Integer>> getFoodPointsUseRegistryKey(Set<Map.Entry<Item, Integer>> instance) {
        return Villagers.ITEM_FOOD_POINTS.entrySet()
            .stream()
            .map(entry -> {
                Item item = this.level().itematic$getItem(entry.getKey()).value();
                return new AbstractMap.SimpleImmutableEntry<>(item, entry.getValue());
            });
    }

    @Override
    protected void fillRecipes(LootContext context) {
        TagKey<Trade> tag = this.getVillagerData().itematic$tradeTag();
        if (tag == null) {
            return;
        }

        Registry<Trade> trades = context.getLevel()
            .registryAccess()
            .lookupOrThrow(ItematicRegistryKeys.TRADE);
        this.fillRecipesFromPool(trades.getOrThrow(tag), 2, context);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.VILLAGER_SPAWN_EGG;
    }
}
