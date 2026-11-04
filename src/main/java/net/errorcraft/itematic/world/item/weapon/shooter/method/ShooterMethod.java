package net.errorcraft.itematic.world.item.weapon.shooter.method;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.component.ItemDamageRules;
import net.errorcraft.itematic.world.item.component.ProjectileComponent;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.OptionalInt;

public interface ShooterMethod<T extends ShooterMethod<T>> {
    Codec<ShooterMethod<?>> CODEC = ItematicBuiltInRegistries.SHOOTER_METHOD.byNameCodec();
    StreamCodec<RegistryFriendlyByteBuf, ShooterMethod<?>> STREAM_CODEC = ByteBufCodecs.registry(ItematicRegistries.SHOOTER_METHOD);

    MapCodec<? extends Properties<T>> propertiesCodec();
    boolean tryShoot(ItemStack stack, Level level, LivingEntity user, InteractionHand hand);
    void hold(ItemStack stack, Level level, LivingEntity user, int usedTicks);
    boolean stop(ItemStack stack, Level level, LivingEntity user, int usedTicks);
    default void initializeProjectile(LivingEntity user, Projectile projectile, int index, float power, float uncertainty, float angle, boolean critical, @Nullable LivingEntity target) {
        if (critical && projectile instanceof AbstractArrow persistentProjectile) {
            persistentProjectile.setCritArrow(true);
        }
    }
    OptionalInt useDuration(ItemStack stack, LivingEntity user);

    default void shoot(ServerLevel level, LivingEntity user, InteractionHand hand, ItemStack shooterStack, List<ItemStack> projectiles, float power, float divergence, boolean critical, @Nullable LivingEntity target) {
        float maxAngle = EnchantmentHelper.processProjectileSpread(level, shooterStack, user, 0.0f);
        float angleStep = projectiles.size() == 1 ?
            0.0f :
            2.0f * maxAngle / (projectiles.size() - 1);
        float angleOffset = ((projectiles.size() - 1) % 2.0f) * angleStep / 2.0f;
        float direction = 1.0f;
        for (int i = 0; i < projectiles.size(); i++) {
            ItemStack projectile = projectiles.get(i);
            if (projectile.isEmpty()) {
                continue;
            }

            float angle = angleOffset + direction * ((i + 1) / 2.0f) * angleStep;
            direction *= -1;
            damageItem(shooterStack, level, hand, user);
            this.createProjectile(projectile, level, user, power, divergence, angle, i, critical, target);
        }
    }

    default void createProjectile(ItemStack projectile, ServerLevel level, LivingEntity user, float power, float divergence, float angle, int index, boolean critical, @Nullable LivingEntity target) {
        Entity entity = ProjectileComponent.spawn(projectile, level, user, 0.0f, power);
        if (entity == null) {
            return;
        }

        if (entity instanceof Projectile projectileEntity) {
            this.initializeProjectile(user, projectileEntity, index, power, divergence, angle, critical, target);
        }
    }

    private static void damageItem(ItemStack stack, ServerLevel level, InteractionHand hand, LivingEntity user) {
        ItemDamageRules rules = stack.get(ItematicDataComponents.SHOOTER_DAMAGE_RULES);
        if (rules == null) {
            return;
        }

        int damage = rules.damage(stack);
        if (damage == 0) {
            return;
        }

        ActionContext context = ActionContext.builder(level)
            .stackExchanger(user, stack)
            .add(LootContextParams.THIS_ENTITY, user)
            .add(LootContextParams.ORIGIN, user.position())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextKeys.HAND, hand)
            .build();
        stack.itematic$damage(damage, context);
    }

    interface Properties<T extends ShooterMethod<T>> {
        Codec<Properties<?>> CODEC = ShooterMethod.CODEC.dispatch(Properties::method, ShooterMethod::propertiesCodec);

        T method();
        void addComponents(DataComponentMap.Builder builder);
    }
}
