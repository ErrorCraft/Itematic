package net.errorcraft.itematic.mixin.entity.passive;

import com.google.common.collect.Lists;
import com.mojang.serialization.DynamicOps;
import net.errorcraft.itematic.loot.context.ItematicLootContextTypes;
import net.errorcraft.itematic.village.trade.Trade;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryOps;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityExtender extends PassiveEntity {
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

    @ModifyArg(
        method = "readCustomDataFromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private DynamicOps<NbtElement> parseTradeOfferListUseCodec(DynamicOps<NbtElement> ops) {
        return RegistryOps.of(ops, this.getWorld().getRegistryManager());
    }

    @Unique
    protected void fillRecipes(LootContext context) {}

    @Unique
    protected void fillRecipesFromContext() {
        if (!(this.getWorld() instanceof ServerWorld world)) {
            return;
        }
        LootContextParameterSet set = new LootContextParameterSet.Builder(world)
            .add(LootContextParameters.THIS_ENTITY, this)
            .add(LootContextParameters.ORIGIN, this.getPos())
            .build(ItematicLootContextTypes.TRADE);
        LootContext context = new LootContext.Builder(set).build(Optional.empty());
        this.fillRecipes(context);
    }

    @Unique
    protected void fillRecipesFromPool(RegistryEntryList.Named<Trade> entries, int count, LootContext context) {
        int actualCount = Math.min(count, entries.size());
        ArrayList<RegistryEntry<Trade>> pool = Lists.newArrayList(entries);
        TradeOfferList recipeList = this.getOffers();
        for (int i = 0; i < actualCount; i++) {
            TradeOffer tradeOffer = pool.remove(this.random.nextInt(pool.size())).value().createTradeOffer(context);
            recipeList.add(tradeOffer);
        }
    }
}
