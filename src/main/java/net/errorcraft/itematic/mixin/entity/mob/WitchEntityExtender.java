package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Witch.class)
public abstract class WitchEntityExtender extends MobEntityExtender {
    protected WitchEntityExtender(EntityType<? extends Raider> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForPotionUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.POTION);
    }

    @Redirect(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/alchemy/PotionContents;createItemStack(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPotionUseCreateStack(Item item, Holder<Potion> potion) {
        return PotionContentsUtil.setPotion(this.level().itematic$createStack(ItemIds.POTION), potion);
    }

    @Redirect(
        method = "performRangedAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/alchemy/PotionContents;createItemStack(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForSplashPotionUseCreateStack(Item item, Holder<Potion> potion) {
        return PotionContentsUtil.setPotion(this.level().itematic$createStack(ItemIds.SPLASH_POTION), potion);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.WITCH_SPAWN_EGG;
    }
}
