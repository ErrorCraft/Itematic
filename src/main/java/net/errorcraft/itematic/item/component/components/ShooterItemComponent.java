package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.ItemDamageRulesDataComponent;
import net.errorcraft.itematic.component.type.ItemListDataComponent;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.shooter.method.ShooterMethod;
import net.errorcraft.itematic.item.use.provider.providers.ShooterIntegerProvider;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.component.ComponentMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public record ShooterItemComponent(RegistryEntryList<Item> heldAmmunition, RegistryEntryList<Item> ammunition, int range, ShooterMethod method, ItemDamageRulesDataComponent itemDamage) implements ItemComponent<ShooterItemComponent> {
    public static final Codec<ShooterItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("held_ammunition").forGetter(ShooterItemComponent::heldAmmunition),
        RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("ammunition").forGetter(ShooterItemComponent::ammunition),
        Codecs.POSITIVE_INT.fieldOf("range").forGetter(ShooterItemComponent::range),
        ShooterMethod.CODEC.fieldOf("method").forGetter(ShooterItemComponent::method),
        ItemDamageRulesDataComponent.CODEC.fieldOf("item_damage").forGetter(ShooterItemComponent::itemDamage)
    ).apply(instance, ShooterItemComponent::new));

    public static ItemComponent<?>[] of(UseAction animation, RegistryEntryList<Item> heldAmmunition, RegistryEntryList<Item> ammunition, int range, ShooterMethod method, ItemDamageRulesDataComponent.Rule... rules) {
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
                new ItemDamageRulesDataComponent(List.of(rules), 1)
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
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (this.method.tryShoot(this, stack, world, user, hand)) {
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        this.method.hold(this, stack, world, user, usedTicks);
    }

    @Override
    public boolean stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {
        return this.method.stop(this, stack, world, user, usedTicks);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(ItematicDataComponentTypes.SHOOTER_AMMUNITION, new ItemListDataComponent(this.ammunition));
        builder.add(ItematicDataComponentTypes.SHOOTER_HELD_AMMUNITION, new ItemListDataComponent(this.heldAmmunition));
        builder.add(ItematicDataComponentTypes.SHOOTER_DAMAGE_RULES, this.itemDamage);
        this.method.addComponents(builder);
    }

    public void shoot(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack shooterStack, List<ItemStack> projectiles, float power, float divergence, boolean critical, @Nullable LivingEntity target) {
        float maxAngle = EnchantmentHelper.getProjectileSpread(world, shooterStack, shooter, 0.0f);
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

    private void damageItem(ItemStack stack, ServerWorld world, Hand hand, LivingEntity shooter) {
        ItemDamageRulesDataComponent rules = stack.get(ItematicDataComponentTypes.SHOOTER_DAMAGE_RULES);
        if (rules == null) {
            return;
        }

        int damage = rules.damage(stack);
        if (damage == 0) {
            return;
        }

        ActionContext context = ActionContext.builder(world)
            .stack(stack)
            .hand(hand)
            .entityPosition(ActionContextParameter.THIS, shooter)
            .build();
        stack.itematic$damage(damage, context);
    }

    private void createProjectile(ItemStack projectile, ServerWorld world, LivingEntity shooter, float power, float divergence, float angle, int index, boolean critical, @Nullable LivingEntity target) {
        Optional<Entity> optionalEntity = projectile.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(projectileComponent -> projectileComponent.createEntity(world, shooter, projectile, 0.0f, power));
        if (optionalEntity.isEmpty()) {
            return;
        }

        Entity entity = optionalEntity.get();
        if (entity instanceof ProjectileEntity projectileEntity) {
            this.method.initializeProjectile(shooter, projectileEntity, index, power, divergence, angle, critical, target);
        }

        world.spawnEntity(entity);
    }
}
