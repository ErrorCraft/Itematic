package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.shooter.method.ShooterMethod;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.item.use.provider.providers.ShooterIntegerProvider;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.component.ItemDamageRules;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public record ShooterItemComponent(HolderSet<Item> heldAmmunition, HolderSet<Item> ammunition, int range, ShooterMethod method, ItemDamageRules itemDamage) implements ItemComponent<ShooterItemComponent> {
    public static final Codec<ShooterItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("held_ammunition").forGetter(ShooterItemComponent::heldAmmunition),
        RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("ammunition").forGetter(ShooterItemComponent::ammunition),
        ExtraCodecs.POSITIVE_INT.fieldOf("range").forGetter(ShooterItemComponent::range),
        ShooterMethod.CODEC.fieldOf("method").forGetter(ShooterItemComponent::method),
        ItemDamageRules.CODEC.fieldOf("item_damage").forGetter(ShooterItemComponent::itemDamage)
    ).apply(instance, ShooterItemComponent::new));

    public static ItemComponent<?>[] of(ItemUseAnimation animation, HolderSet<Item> heldAmmunition, HolderSet<Item> ammunition, int range, ShooterMethod method, ItemDamageRules.Rule... rules) {
        return new ItemComponent<?>[] {
            UseableItemComponent.builder()
                .useFor(ShooterIntegerProvider.INSTANCE)
                .animation(animation)
                .build(),
            new ShooterItemComponent(
                heldAmmunition,
                ammunition,
                range,
                method,
                new ItemDamageRules(List.of(rules), 1)
            )
        };
    }

    @Override
    public ItemComponentType<ShooterItemComponent> type() {
        return ItemComponentTypes.SHOOTER;
    }

    @Override
    public Codec<ShooterItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.method.tryShoot(this, stack, world, user, hand)) {
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }

    @Override
    public void using(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        this.method.hold(this, stack, world, user, usedTicks);
    }

    @Override
    public boolean stopUsing(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackExchanger stackExchanger) {
        return this.method.stop(this, stack, world, user, usedTicks);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.SHOOTER_AMMUNITION, this.ammunition);
        builder.set(ItematicDataComponents.SHOOTER_HELD_AMMUNITION, this.heldAmmunition);
        builder.set(ItematicDataComponents.SHOOTER_DAMAGE_RULES, this.itemDamage);
        this.method.addComponents(builder);
    }

    public boolean usesMethod(ShooterMethodType<?> type) {
        return this.method.type() == type;
    }

    public void shoot(ServerLevel world, LivingEntity shooter, InteractionHand hand, ItemStack shooterStack, List<ItemStack> projectiles, float power, float divergence, boolean critical, @Nullable LivingEntity target) {
        float maxAngle = EnchantmentHelper.processProjectileSpread(world, shooterStack, shooter, 0.0f);
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
            this.damageItem(shooterStack, world, hand, shooter);
            this.createProjectile(projectile, world, shooter, power, divergence, angle, i, critical, target);
        }
    }

    public OptionalInt useDuration(ItemStack stack, LivingEntity user) {
        return this.method.useDuration(stack, user);
    }

    private void damageItem(ItemStack stack, ServerLevel world, InteractionHand hand, LivingEntity shooter) {
        ItemDamageRules rules = stack.get(ItematicDataComponents.SHOOTER_DAMAGE_RULES);
        if (rules == null) {
            return;
        }

        int damage = rules.damage(stack);
        if (damage == 0) {
            return;
        }

        ActionContext context = ActionContext.builder(world)
            .stackExchanger(shooter, stack)
            .add(LootContextParams.THIS_ENTITY, shooter)
            .add(LootContextParams.ORIGIN, shooter.position())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextParameters.HAND, hand)
            .build();
        stack.itematic$damage(damage, context);
    }

    private void createProjectile(ItemStack projectile, ServerLevel world, LivingEntity shooter, float power, float divergence, float angle, int index, boolean critical, @Nullable LivingEntity target) {
        Optional<Entity> optionalEntity = projectile.itematic$getBehavior(ItemComponentTypes.PROJECTILE)
            .map(projectileComponent -> projectileComponent.spawnEntity(world, shooter, projectile, 0.0f, power));
        if (optionalEntity.isEmpty()) {
            return;
        }

        Entity entity = optionalEntity.get();
        if (entity instanceof Projectile projectileEntity) {
            this.method.initializeProjectile(shooter, projectileEntity, index, power, divergence, angle, critical, target);
        }
    }
}
