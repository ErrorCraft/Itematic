package net.errorcraft.itematic.mixin.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public interface RangedWeaponItemAccessor {
    @Invoker("draw")
    static List<ItemStack> load(ItemStack stack, ItemStack projectile, LivingEntity shooter) {
        throw new AssertionError();
    }

    @Invoker("shootProjectile")
    void shoot(LivingEntity user, Projectile projectile, int index, float power, float uncertainty, float angle, @Nullable LivingEntity target);
}
