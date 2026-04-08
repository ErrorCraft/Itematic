package net.errorcraft.itematic.mixin.entity.passive;

import com.google.common.collect.Lists;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.errorcraft.itematic.util.context.ItematicContextTypes;
import net.errorcraft.itematic.village.trade.Trade;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityExtender extends MobEntityExtender {
    @Shadow
    public abstract TradeOfferList getOffers();

    protected MerchantEntityExtender(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getOffers",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/MerchantEntity;fillRecipes()V"
        )
    )
    private void fillRecipesUseDynamicRegistry(MerchantEntity instance) {
        this.fillRecipesFromContext();
    }

    @Unique
    protected void fillRecipes(LootContext context) {}

    @Unique
    protected void fillRecipesFromContext() {
        if (!(this.getWorld() instanceof ServerWorld world)) {
            return;
        }

        LootWorldContext set = new LootWorldContext.Builder(world)
            .add(LootContextParameters.THIS_ENTITY, this)
            .add(LootContextParameters.ORIGIN, this.getPos())
            .build(ItematicContextTypes.TRADE);
        LootContext context = new LootContext.Builder(set).build(Optional.empty());
        this.fillRecipes(context);
    }

    @Unique
    protected void fillRecipesFromPool(RegistryEntryList.Named<Trade> entries, int count, LootContext context) {
        int actualCount = Math.min(count, entries.size());
        ArrayList<RegistryEntry<Trade>> pool = Lists.newArrayList(entries);
        TradeOfferList recipeList = this.getOffers();
        int addedTrades = 0;
        while (addedTrades < actualCount && !pool.isEmpty()) {
            TradeOffer tradeOffer = pool.remove(this.random.nextInt(pool.size()))
                .value()
                .createTradeOffer(context);
            if (tradeOffer == null) {
                continue;
            }

            recipeList.add(tradeOffer);
            addedTrades++;
        }
    }
}
