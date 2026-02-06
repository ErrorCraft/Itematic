package net.errorcraft.itematic.mixin.entity.passive;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.entity.passive.VillagerEntityUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trade;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityExtender extends MerchantEntityExtender {
    @Shadow
    public abstract VillagerData getVillagerData();

    protected VillagerEntityExtender(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "canGather",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableSet;contains(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean containsForGatherableItemsUseItemTagCheck(ImmutableSet<Item> instance, Object o, @Local(argsOnly = true) ItemStack stack) {
        TagKey<Item> tag = this.getVillagerData().getProfession().itematic$gatherableItemsTag();
        if (tag == null) {
            return false;
        }

        return stack.isIn(tag);
    }

    @Redirect(
        method = "levelUp",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/VillagerEntity;fillRecipes()V"
        )
    )
    private void fillRecipesUseDynamicRegistry(VillagerEntity instance) {
        this.fillRecipesFromContext();
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForVillagerSpawnEggUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.VILLAGER_SPAWN_EGG);
    }

    @Redirect(
        method = "consumeAvailableFood",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <K, V> V getFoodPointsUseRegistryKey(Map<K, V> instance, Object o, @Local ItemStack stack) {
        return (V) VillagerEntityUtil.ITEM_FOOD_POINTS.get(stack.itematic$key());
    }

    @Redirect(
        method = "getAvailableFood",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;stream()Ljava/util/stream/Stream;"
        )
    )
    private Stream<Map.Entry<Item, Integer>> getFoodPointsUseRegistryKey(Set<Map.Entry<Item, Integer>> instance) {
        return VillagerEntityUtil.ITEM_FOOD_POINTS.entrySet()
            .stream()
            .map(entry -> {
                Item item = this.getWorld().itematic$getItem(entry.getKey()).value();
                return new AbstractMap.SimpleImmutableEntry<>(item, entry.getValue());
            });
    }

    @Override
    protected void fillRecipes(LootContext context) {
        TagKey<Trade> tag = this.getVillagerData().itematic$tradeTag();
        if (tag == null) {
            return;
        }

        Registry<Trade> trades = context.getWorld()
            .getRegistryManager()
            .getOrThrow(ItematicRegistryKeys.TRADE);
        this.fillRecipesFromPool(trades.getOrThrow(tag), 2, context);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.VILLAGER_SPAWN_EGG;
    }
}
