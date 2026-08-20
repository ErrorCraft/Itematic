package net.errorcraft.itematic.mixin.world.entity.npc.villager;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.npc.villager.Villagers;
import net.errorcraft.itematic.world.item.trading.Trade;
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
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(Villager.class)
public abstract class VillagerExtender extends AbstractVillagerExtender {
    @Shadow
    public abstract VillagerData getVillagerData();

    protected VillagerExtender(EntityType<? extends AgeableMob> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "wantsToPickUp",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableSet;contains(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean isRequestedItemCheckTag(ImmutableSet<Item> instance, Object o, @Local(argsOnly = true) ItemStack stack) {
        TagKey<Item> requestedItems = this.getVillagerData().profession().value().itematic$gatherableItems();
        if (requestedItems == null) {
            return false;
        }

        return stack.is(requestedItems);
    }

    @Redirect(
        method = "increaseMerchantCareer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;updateTrades(Lnet/minecraft/server/level/ServerLevel;)V"
        )
    )
    private void fillRecipesUseDynamicRegistry(Villager instance, ServerLevel level) {
        this.fillRecipesFromContext(level);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isVillagerSpawnEggCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.VILLAGER_SPAWN_EGG);
    }

    @Redirect(
        method = "eatUntilFull",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <K, V> V getFoodPointsUseId(Map<K, V> instance, Object o, @Local ItemStack stack) {
        return (V) Villagers.ITEM_FOOD_POINTS.get(stack.itematic$key());
    }

    @Redirect(
        method = "countFoodPointsInInventory",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;stream()Ljava/util/stream/Stream;"
        )
    )
    private Stream<Map.Entry<Item, Integer>> getFoodPointsUseId(Set<Map.Entry<Item, Integer>> instance) {
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
            .lookupOrThrow(ItematicRegistries.TRADE);
        this.fillRecipesFromPool(trades.getOrThrow(tag), 2, context);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.VILLAGER_SPAWN_EGG;
    }
}
