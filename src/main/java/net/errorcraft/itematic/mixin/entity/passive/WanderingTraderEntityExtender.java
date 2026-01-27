package net.errorcraft.itematic.mixin.entity.passive;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.TradeTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityExtender extends MerchantEntityExtender {
    @Unique
    private static final Object2IntMap<TagKey<Trade>> TRADE_TO_AMOUNT = Util.make(new Object2IntArrayMap<>(), trades -> {
        trades.put(TradeTags.WANDERING_TRADER_REGULAR, 5);
        trades.put(TradeTags.WANDERING_TRADER_SPECIAL, 1);
    });

    public WanderingTraderEntityExtender(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/PotionContentsComponent;createStack(Lnet/minecraft/item/Item;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPotionUseCreateStack(Item item, RegistryEntry<Potion> potion) {
        return PotionContentsComponentUtil.setPotion(this.getWorld().itematic$createStack(ItemKeys.POTION), potion);
    }

    @Redirect(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;"
            )
        )
    )
    private ItemStack newItemStackForMilkBucketUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.MILK_BUCKET);
    }

    @Redirect(
        method = "getDrinkSound",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForMilkBucketUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.MILK_BUCKET);
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

    @Override
    protected void fillRecipes(LootContext context) {
        Registry<Trade> trades = context.getWorld().getRegistryManager().get(ItematicRegistryKeys.TRADE);
        for (TagKey<Trade> trade : TRADE_TO_AMOUNT.keySet()) {
            this.fillRecipesFromPool(trades.getEntryList(trade).orElseThrow(), TRADE_TO_AMOUNT.getInt(trade), context);
        }
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.WANDERING_TRADER_SPAWN_EGG;
    }
}
