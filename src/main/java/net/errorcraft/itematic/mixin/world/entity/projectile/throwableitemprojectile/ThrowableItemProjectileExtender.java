package net.errorcraft.itematic.mixin.world.entity.projectile.throwableitemprojectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrowableItemProjectile.class)
public abstract class ThrowableItemProjectileExtender extends ThrowableProjectile {
    protected ThrowableItemProjectileExtender(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = {
            "defineSynchedData",
            "lambda$readAdditionalSaveData$0"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemLike item) {
        return this.level().itematic$createStack(this.getDefaultItemId());
    }

    @Unique
    protected ResourceKey<Item> getDefaultItemId() {
        return ItemIds.AIR;
    }
}
