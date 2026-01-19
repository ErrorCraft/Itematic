package net.errorcraft.itematic.item.shooter.method;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public interface ShooterMethod {
    Codec<ShooterMethod> CODEC = ItematicRegistries.SHOOTER_METHOD_TYPE.getCodec().dispatch(ShooterMethod::type, ShooterMethodType::codec);

    ShooterMethodType<?> type();
    void addComponents(ComponentMap.Builder builder);
    boolean tryShoot(ShooterItemComponent component, ItemStack stack, World world, LivingEntity user, Hand hand);
    void hold(ShooterItemComponent shooter, ItemStack stack, World world, LivingEntity user, int usedTicks);
    void stop(ShooterItemComponent shooter, ItemStack stack, World world, LivingEntity user, int usedTicks);
    default void initializeProjectile(LivingEntity user, ProjectileEntity projectile, int index, float power, float uncertainty, float angle, boolean critical, @Nullable LivingEntity target) {
        if (critical && projectile instanceof PersistentProjectileEntity persistentProjectile) {
            persistentProjectile.setCritical(true);
        }
    }
    OptionalInt useDuration(ItemStack stack, LivingEntity user);
    float pullProgress(ItemStack stack, LivingEntity user, int usedTicks);
}
