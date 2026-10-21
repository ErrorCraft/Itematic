package net.errorcraft.itematic.mixin.world.entity.monster.illager;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.world.entity.projectile.Projectiles;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Direct;
import net.minecraft.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.illager.SpellcasterIllager;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Illusioner.class)
public abstract class IllusionerExtender extends SpellcasterIllager {
    protected IllusionerExtender(EntityType<? extends SpellcasterIllager> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBowUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.BOW);
    }

    @WrapOperation(
        method = "performRangedAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getWeaponHoldingHandForBowUseDataComponent(LivingEntity mob, Item weaponItem, Operation<InteractionHand> original) {
        return Projectiles.getWeaponHoldingHand(mob, Direct.INSTANCE);
    }

    @WrapOperation(
        method = "performRangedAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileUsingShoot(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;DDDFF)Lnet/minecraft/world/entity/projectile/Projectile;"
        )
    )
    private <T extends Projectile> T onlySetSpeed(T projectile, ServerLevel serverLevel, ItemStack itemStack, double targetX, double targetY, double targetZ, float pow, float uncertainty, Operation<T> original) {
        projectile.shoot(targetX, targetY, targetZ, pow, uncertainty);
        return projectile;
    }
}
