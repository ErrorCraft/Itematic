package net.errorcraft.itematic.mixin.world.entity.projectile.throwableitemprojectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Snowball.class)
public abstract class SnowballExtender extends ThrowableItemProjectileExtender {
    public SnowballExtender(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "getParticle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
        )
    )
    private boolean isEmptyCheckInteractableStack(ItemStack instance, Operation<Boolean> original) {
        return instance.itematic$cannotBeInteractedWith();
    }

    @Override
    protected ResourceKey<Item> getDefaultItemId() {
        return ItemIds.SNOWBALL;
    }
}
