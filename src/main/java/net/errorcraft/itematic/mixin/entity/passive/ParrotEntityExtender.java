package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(ParrotEntity.class)
public abstract class ParrotEntityExtender extends MobEntityExtender {
    protected ParrotEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForCookieUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.COOKIE);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean testForTameItemsUseItemTagCheck(Set<Item> instance, Object o, @Local ItemStack stack) {
        return stack.isIn(ItematicItemTags.PARROT_TAME_ITEMS);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.PARROT_SPAWN_EGG;
    }
}
