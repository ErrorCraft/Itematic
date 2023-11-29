package net.errorcraft.itematic.mixin.entity.passive;

import com.google.common.collect.ImmutableSet;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.TradeOfferListUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

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
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean containsForGatherableItemsUseItemTagCheck(Set<Item> instance, Object o, ItemStack stack) {
        return stack.isIn(ItematicItemTags.VILLAGER_GATHERABLE_ITEMS);
    }

    @Redirect(
        method = "canGather",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableSet;contains(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean containsForGatherableItemsUseItemTagCheck(ImmutableSet<Item> instance, Object o, ItemStack stack) {
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
        method = "readCustomDataFromNbt",
        at = @At(
            value = "NEW",
            target = "net/minecraft/village/TradeOfferList"
        )
    )
    private TradeOfferList newTradeOfferListUseCodec(NbtCompound nbt) {
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, this.getWorld().getRegistryManager());
        return TradeOfferListUtil.CODEC.parse(ops, nbt).result().orElse(new TradeOfferList());
    }

    @Override
    protected void fillRecipes(LootContext context) {
        TagKey<Trade> tag = this.getVillagerData().itematic$tradeTag();
        if (tag == null) {
            return;
        }
        Registry<Trade> trades = context.getWorld().getRegistryManager().get(ItematicRegistryKeys.TRADE);
        this.fillRecipesFromPool(trades.getOrCreateEntryList(tag), 2, context);
    }
}
