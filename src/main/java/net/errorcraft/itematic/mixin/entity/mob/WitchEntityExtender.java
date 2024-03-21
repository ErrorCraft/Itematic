package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitchEntity.class)
public abstract class WitchEntityExtender extends MobEntityExtender {
    protected WitchEntityExtender(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForPotionUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.POTION);
    }

    @Redirect(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/PotionContentsComponent;createStack(Lnet/minecraft/item/Item;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPotionUseCreateStack(Item item, RegistryEntry<Potion> potion) {
        return PotionContentsComponentUtil.setPotion(this.getWorld().itematic$createStack(ItemKeys.POTION), potion);
    }

    @Redirect(
        method = "shootAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/PotionContentsComponent;createStack(Lnet/minecraft/item/Item;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForSplashPotionUseCreateStack(Item item, RegistryEntry<Potion> potion) {
        return PotionContentsComponentUtil.setPotion(this.getWorld().itematic$createStack(ItemKeys.SPLASH_POTION), potion);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.WITCH_SPAWN_EGG;
    }
}
