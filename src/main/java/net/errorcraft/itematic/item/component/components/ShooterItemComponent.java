package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.util.ShooterUtil;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Optional;

public record ShooterItemComponent(TagKey<Item> heldAmmunition, TagKey<Item> ammunition, int range, boolean chargeable) implements ItemComponent<ShooterItemComponent> {
    public static final Codec<ShooterItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("held_ammunition").forGetter(ShooterItemComponent::heldAmmunition),
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("ammunition").forGetter(ShooterItemComponent::ammunition),
        Codec.INT.fieldOf("range").forGetter(ShooterItemComponent::range),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "chargeable", false).forGetter(ShooterItemComponent::chargeable)
    ).apply(instance, ShooterItemComponent::new));

    private static final String STARTED_KEY = "started";
    private static final String LOADED_KEY = "loaded";

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
            CrossbowItem.shootAll(world, user, hand, stack, getSpeed(stack), 1.0f);
            CrossbowItem.setCharged(stack, false);
            return ActionResult.CONSUME;
        }
        if (!user.itematic$getAmmunition(this).isEmpty()) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!this.chargeable) {
            return;
        }
        this.tryLoad(stack, world, user, this.getPullProgress(stack, remainingUseTicks));
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {
        float pullProgress = this.getPullProgress(stack, remainingUseTicks);
        if (this.chargeable) {
            this.charge(stack, world, user, pullProgress);
            return;
        }
        if (!(user instanceof PlayerEntity player)) {
            return;
        }
        this.shoot(stack, world, player, pullProgress, resultStackConsumer);
    }

    public static ShooterItemComponent of(TagKey<Item> heldAmmunition, TagKey<Item> ammunition, int range, boolean chargeable) {
        return new ShooterItemComponent(heldAmmunition, ammunition, range, chargeable);
    }

    public boolean isHeldAmmunition(ItemStack stack) {
        return stack.isIn(this.heldAmmunition);
    }

    public boolean isAmmunition(ItemStack stack) {
        return stack.isIn(this.ammunition);
    }

    public float getPullProgress(ItemStack stack, int remainingUseTicks) {
        int useTicks = stack.getMaxUseTime() - remainingUseTicks;
        if (this.chargeable) {
            float progress = (float)useTicks / getPullTime(stack);
            return Math.min(progress, 1.0f);
        }
        return BowItem.getPullProgress(useTicks);
    }

    public boolean isCharged(ItemStack stack) {
        return this.chargeable && CrossbowItem.isCharged(stack);
    }

    public boolean hasLoadedAmmunition(ItemStack stack, DynamicRegistryManager registryManager, RegistryKey<Item> key) {
        return ShooterUtil.getLoadedAmmunition(stack, registryManager).stream().anyMatch(s -> s.itematic$isOf(key));
    }

    private void tryLoad(ItemStack stack, World world, LivingEntity user, float pullProgress) {
        if (world.isClient()) {
            return;
        }
        if (pullProgress == 1.0f) {
            return;
        }
        int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        if (pullProgress >= 0.2f && !isStarted(stack)) {
            setStarted(stack, true);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), getQuickChargeSound(quickChargeLevel), SoundCategory.PLAYERS, 0.5f, 1.0f);
        }
        if (pullProgress >= 0.5f && quickChargeLevel == 0 && !isLoaded(stack)) {
            setLoaded(stack, true);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE, SoundCategory.PLAYERS, 0.5f, 1.0f);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void charge(ItemStack stack, World world, LivingEntity user, float pullProgress) {
        setStarted(stack, false);
        setLoaded(stack, false);
        if (pullProgress == 1.0f && !this.isCharged(stack) && CrossbowItemAccessor.loadProjectiles(user, stack)) {
            CrossbowItem.setCharged(stack, true);
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
        if (entity instanceof ArrowEntity arrowEntity) {
            arrowEntity.initFromStack(stack);
        }
        if (entity instanceof PersistentProjectileEntity persistentProjectileEntity) {
            this.initProjectile(persistentProjectileEntity, stack, pullProgress);

            ActionContext context = MutableActionContext.stackUsage(world, stack, resultStackConsumer, player.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, player);
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

    private static int getPullTime(ItemStack stack) {
        int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return stack.getMaxUseTime() - 5 * quickChargeLevel + 3;
    }

    private static float getSpeed(ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(ProjectileItemComponent::chargedSpeed)
            .orElse(CrossbowItemAccessor.getDefaultSpeed());
    }

    private static SoundEvent getQuickChargeSound(int level) {
        return switch (level) {
            case 1 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            case 2 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            case 3 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            default -> SoundEvents.ITEM_CROSSBOW_LOADING_START;
        };
    }

    private static void setStarted(ItemStack stack, boolean started) {
        stack.getOrCreateNbt().putBoolean(STARTED_KEY, started);
    }

    private static boolean isStarted(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean(STARTED_KEY);
    }

    private static void setLoaded(ItemStack stack, boolean loaded) {
        stack.getOrCreateNbt().putBoolean(LOADED_KEY, loaded);
    }

    private static boolean isLoaded(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean(LOADED_KEY);
    }
}
