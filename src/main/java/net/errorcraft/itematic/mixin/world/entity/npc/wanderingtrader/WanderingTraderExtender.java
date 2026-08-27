package net.errorcraft.itematic.mixin.world.entity.npc.wanderingtrader;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderExtender extends MobExtender {
    protected WanderingTraderExtender(EntityType<? extends LivingEntity> type, Level level) {
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
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isVillagerSpawnEggCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.VILLAGER_SPAWN_EGG);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.WANDERING_TRADER_SPAWN_EGG;
    }
}
