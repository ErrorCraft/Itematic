package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ProjectileItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemExtender {
    @Redirect(
        method = "loadProjectiles",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack getProjectileTypeUseItemComponent(LivingEntity instance, ItemStack stack) {
        if (stack.itematic$hasComponent(ItemComponentTypes.SHOOTER)) {
            return instance.itematic$getAmmunition(stack);
        }
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "createArrowEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForFireworkRocketUseItemComponent(ItemStack instance, Item item) {
        return instance.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(ProjectileItemComponent::entity)
            .map(EntityInitializer::type)
            .map(e -> e == EntityType.FIREWORK_ROCKET)
            .orElse(false);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getWeaponStackDamage(ItemStack projectile) {
        return projectile.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(ProjectileItemComponent::damage)
            .orElse(0);
    }
}
