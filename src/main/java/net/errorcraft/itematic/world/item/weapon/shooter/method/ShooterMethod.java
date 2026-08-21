package net.errorcraft.itematic.world.item.weapon.shooter.method;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.OptionalInt;

public interface ShooterMethod {
    Codec<ShooterMethod> CODEC = ItematicBuiltInRegistries.SHOOTER_METHOD_TYPE.byNameCodec()
        .dispatch(ShooterMethod::type, ShooterMethodType::codec);

    ShooterMethodType<?> type();
    void addComponents(DataComponentMap.Builder builder);
    boolean tryShoot(ShooterItemBehavior component, ItemStack stack, Level level, LivingEntity user, InteractionHand hand);
    void hold(ShooterItemBehavior shooter, ItemStack stack, Level level, LivingEntity user, int usedTicks);
    boolean stop(ShooterItemBehavior shooter, ItemStack stack, Level level, LivingEntity user, int usedTicks);
    default void initializeProjectile(LivingEntity user, Projectile projectile, int index, float power, float uncertainty, float angle, boolean critical, @Nullable LivingEntity target) {
        if (critical && projectile instanceof AbstractArrow persistentProjectile) {
            persistentProjectile.setCritArrow(true);
        }
    }
    OptionalInt useDuration(ItemStack stack, LivingEntity user);
    float pullProgress(ItemStack stack, LivingEntity user, int usedTicks);
}
