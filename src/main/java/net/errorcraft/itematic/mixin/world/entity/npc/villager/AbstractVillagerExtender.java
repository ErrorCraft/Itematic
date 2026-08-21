package net.errorcraft.itematic.mixin.world.entity.npc.villager;

import com.google.common.collect.Lists;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.util.context.ItematicContextKeySets;
import net.errorcraft.itematic.world.item.trading.Trade;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerExtender extends MobExtender {
    @Shadow
    public abstract MerchantOffers getOffers();

    protected AbstractVillagerExtender(EntityType<? extends AgeableMob> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getOffers",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/AbstractVillager;updateTrades(Lnet/minecraft/server/level/ServerLevel;)V"
        )
    )
    private void fillRecipesUseDynamicRegistry(AbstractVillager instance, ServerLevel level) {
        this.fillRecipesFromContext(level);
    }

    @Unique
    protected void fillRecipes(LootContext context) {}

    @Unique
    protected void fillRecipesFromContext(ServerLevel level) {
        LootParams set = new LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, this)
            .withParameter(LootContextParams.ORIGIN, this.position())
            .create(ItematicContextKeySets.TRADE);
        LootContext context = new LootContext.Builder(set).create(Optional.empty());
        this.fillRecipes(context);
    }

    @Unique
    protected void fillRecipesFromPool(HolderSet.Named<Trade> entries, int count, LootContext context) {
        int actualCount = Math.min(count, entries.size());
        ArrayList<Holder<Trade>> pool = Lists.newArrayList(entries);
        MerchantOffers offers = this.getOffers();
        int addedTrades = 0;
        while (addedTrades < actualCount && !pool.isEmpty()) {
            MerchantOffer merchantOffer = pool.remove(this.random.nextInt(pool.size()))
                .value()
                .createMerchantOffer(context);
            if (merchantOffer == null) {
                continue;
            }

            offers.add(merchantOffer);
            addedTrades++;
        }
    }
}
