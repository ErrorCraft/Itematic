package net.errorcraft.itematic.mixin.world.entity.npc.wanderingtrader;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.mixin.world.entity.npc.villager.AbstractVillagerExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.TradeTags;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderExtender extends AbstractVillagerExtender {
    @Unique
    private static final Object2IntMap<TagKey<Trade>> TRADE_TO_AMOUNT = Util.make(new Object2IntArrayMap<>(), trades -> {
        trades.put(TradeTags.WANDERING_TRADER_BUYING, 2);
        trades.put(TradeTags.WANDERING_TRADER_SPECIAL, 2);
        trades.put(TradeTags.WANDERING_TRADER_REGULAR, 5);
    });

    public WanderingTraderExtender(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "registerGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/alchemy/PotionContents;createItemStack(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPotionUseCreateStack(Item item, Holder<Potion> potion) {
        return PotionContentsUtil.setPotion(this.level().itematic$createStack(ItemIds.POTION), potion);
    }

    @Redirect(
        method = "registerGoals",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;POTION:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForMilkBucketUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.MILK_BUCKET);
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

    @Override
    protected void fillRecipes(LootContext context) {
        Registry<Trade> trades = context.getLevel().registryAccess().lookupOrThrow(ItematicRegistries.TRADE);
        for (TagKey<Trade> trade : TRADE_TO_AMOUNT.keySet()) {
            this.fillRecipesFromPool(trades.getOrThrow(trade), TRADE_TO_AMOUNT.getInt(trade), context);
        }
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.WANDERING_TRADER_SPAWN_EGG;
    }
}
