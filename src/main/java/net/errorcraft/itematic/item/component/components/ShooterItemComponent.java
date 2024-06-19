package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record ShooterItemComponent(TagKey<Item> heldAmmunition, TagKey<Item> ammunition, int range, Optional<Chargeable> chargeable) implements ItemComponent<ShooterItemComponent> {
    public static final Codec<ShooterItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("held_ammunition").forGetter(ShooterItemComponent::heldAmmunition),
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("ammunition").forGetter(ShooterItemComponent::ammunition),
        Codecs.POSITIVE_INT.fieldOf("range").forGetter(ShooterItemComponent::range),
        Chargeable.CODEC.optionalFieldOf("chargeable").forGetter(ShooterItemComponent::chargeable)
    ).apply(instance, ShooterItemComponent::new));
    private static final float CHARGE_PROGRESS = CrossbowItemAccessor.chargeProgress();
    private static final float LOAD_PROGRESS = CrossbowItemAccessor.loadProgress();
    private static final int DEFAULT_CHARGE_TIME = CrossbowItemAccessor.defaultPullTime();
    private static final int EXTRA_USE_TIME = 3;
    private static final int CHARGE_TIME_PER_QUICK_CHARGE_LEVEL = 5;
    private static final CrossbowItem DUMMY = new CrossbowItem(new Item.Settings());

    public static ShooterItemComponent of(TagKey<Item> heldAmmunition, TagKey<Item> ammunition, int range) {
        return new ShooterItemComponent(heldAmmunition, ammunition, range, Optional.empty());
    }

    @SafeVarargs
    public static ShooterItemComponent of(TagKey<Item> heldAmmunition, TagKey<Item> ammunition, int range, RegistryEntry<SoundEvent> defaultSound, RegistryEntry<SoundEvent>... levelSounds) {
        return new ShooterItemComponent(heldAmmunition, ammunition, range, Optional.of(Chargeable.of(QuickChargeSounds.of(defaultSound, levelSounds))));
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
        if (this.isCharged(stack)) {
            float chargedSpeed = stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
                .itematic$getChargedSpeed();
            DUMMY.shootAll(world, user, hand, stack, chargedSpeed, 1.0f, null);
            return ActionResult.CONSUME;
        }
        if (!user.itematic$getAmmunition(this).isEmpty()) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        this.chargeable.ifPresent(chargeable -> this.tryLoad(stack, world, user, usedTicks, chargeable));
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {
        float pullProgress = this.getPullProgress(stack, usedTicks);
        if (this.isChargeable()) {
            this.charge(stack, world, user, pullProgress);
            return;
        }
        if (!(user instanceof PlayerEntity player)) {
            return;
        }
        this.shoot(stack, world, player, pullProgress, resultStackConsumer);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        if (this.isChargeable()) {
            builder.add(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        }
    }

    public static int useDuration(ItemStack stack) {
        return getPullTime(stack) + EXTRA_USE_TIME;
    }

    public static int getPullTime(ItemStack stack) {
        int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return DEFAULT_CHARGE_TIME - CHARGE_TIME_PER_QUICK_CHARGE_LEVEL * quickChargeLevel;
    }

    public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, @Nullable LivingEntity target) {
        DUMMY.shootAll(world, shooter, hand, stack, speed, divergence, target);
    }

    public boolean isHeldAmmunition(ItemStack stack) {
        return stack.isIn(this.heldAmmunition);
    }

    public boolean isAmmunition(ItemStack stack) {
        return stack.isIn(this.ammunition);
    }

    public float getPullProgress(ItemStack stack, int usedTicks) {
        if (this.isChargeable()) {
            float progress = (float)usedTicks / getPullTime(stack);
            return Math.min(progress, 1.0f);
        }
        return BowItem.getPullProgress(usedTicks);
    }

    public boolean isChargeable() {
        return this.chargeable.isPresent();
    }

    public boolean isCharged(ItemStack stack) {
        return this.isChargeable() && !stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT).isEmpty();
    }

    private void tryLoad(ItemStack stack, World world, LivingEntity user, int usedTicks, Chargeable chargeable) {
        if (world.isClient()) {
            return;
        }
        int pullTime = getPullTime(stack);
        if (usedTicks >= pullTime) {
            return;
        }
        int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        if (usedTicks == getPullTimeAt(pullTime, CHARGE_PROGRESS)) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), chargeable.quickChargeSounds.get(quickChargeLevel).value(), SoundCategory.PLAYERS, 0.5f, 1.0f);
            return;
        }
        if (usedTicks == getPullTimeAt(pullTime, LOAD_PROGRESS) && quickChargeLevel == 0) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE, SoundCategory.PLAYERS, 0.5f, 1.0f);
        }
    }

    private void charge(ItemStack stack, World world, LivingEntity user, float pullProgress) {
        if (pullProgress == 1.0f && !this.isCharged(stack) && CrossbowItemAccessor.loadProjectiles(user, stack)) {
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
        }
    }

    private void shoot(ItemStack stack, World world, PlayerEntity player, float pullProgress, ItemStackConsumer resultStackConsumer) {
        if (pullProgress < 0.1f) {
            return;
        }
        ItemStack ammunition = player.itematic$getAmmunition(this);
        if (ammunition.isEmpty()) {
            return;
        }
        boolean disallowPickup = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 && ammunition.itematic$isOf(ItemKeys.ARROW);
        if (!world.isClient()) {
            this.createProjectile(stack, ammunition, (ServerWorld) world, player, pullProgress, disallowPickup, resultStackConsumer);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        if (player.getAbilities().creativeMode || disallowPickup) {
            return;
        }
        ammunition.decrement(1);
        if (ammunition.isEmpty()) {
            player.getInventory().removeOne(ammunition);
        }
    }

    private void createProjectile(ItemStack stack, ItemStack ammunition, ServerWorld world, PlayerEntity player, float pullProgress, boolean disallowPickup, ItemStackConsumer resultStackConsumer) {
        Optional<Entity> optionalEntity = ammunition.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(c -> c.createEntity(world, player, ammunition, 0.0f, pullProgress * 3.0f));

        if (optionalEntity.isEmpty()) {
            return;
        }

        Entity entity = optionalEntity.get();
        if (entity instanceof PersistentProjectileEntity persistentProjectileEntity) {
            this.initProjectile(persistentProjectileEntity, stack, pullProgress);

            ActionContext context = ActionContext.builder(world, stack, resultStackConsumer, player.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, player)
                .build();
            stack.itematic$damage(1, context);
            if (disallowPickup) {
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
        }

        world.spawnEntity(entity);
    }

    private void initProjectile(PersistentProjectileEntity entity, ItemStack stack, float pullProgress) {
        if (pullProgress == 1.0f) {
            entity.setCritical(true);
        }
        int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
        if (powerLevel > 0) {
            entity.setDamage(entity.getDamage() + powerLevel * 0.5d + 0.5d);
        }
        int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
        if (punchLevel > 0) {
            entity.setPunch(punchLevel);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
            entity.setOnFireFor(100);
        }
    }

    private static int getPullTimeAt(int pullTime, float progress) {
        return (int)(progress * pullTime);
    }

    public record Chargeable(QuickChargeSounds quickChargeSounds) {
        public static final Codec<Chargeable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            QuickChargeSounds.CODEC.fieldOf("quick_charge_sounds").forGetter(Chargeable::quickChargeSounds)
        ).apply(instance, Chargeable::new));

        public static Chargeable of(QuickChargeSounds quickChargeSounds) {
            return new Chargeable(quickChargeSounds);
        }
    }

    public record QuickChargeSounds(List<RegistryEntry<SoundEvent>> levels, RegistryEntry<SoundEvent> defaultSound) {
        public static final Codec<QuickChargeSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.ENTRY_CODEC.listOf().fieldOf("levels").forGetter(QuickChargeSounds::levels),
            SoundEvent.ENTRY_CODEC.fieldOf("default").forGetter(QuickChargeSounds::defaultSound)
        ).apply(instance, QuickChargeSounds::new));

        public RegistryEntry<SoundEvent> get(int level) {
            if (level < 0 || level >= this.levels.size()) {
                return this.defaultSound;
            }
            return this.levels.get(level);
        }

        @SafeVarargs
        public static QuickChargeSounds of(RegistryEntry<SoundEvent> defaultSound, RegistryEntry<SoundEvent>... levelSounds) {
            return new QuickChargeSounds(List.of(levelSounds), defaultSound);
        }
    }
}
