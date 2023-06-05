package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Optional;

public record ShooterItemComponent(TagKey<Item> heldAmmunition, TagKey<Item> ammunition, boolean chargeable) implements ItemComponent {
    public static final Codec<ShooterItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("held_ammunition").forGetter(ShooterItemComponent::heldAmmunition),
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("ammunition").forGetter(ShooterItemComponent::ammunition),
        Codec.BOOL.optionalFieldOf("chargeable", false).forGetter(ShooterItemComponent::chargeable)
    ).apply(instance, ShooterItemComponent::new));

    private static final String STARTED_KEY = "started";
    private static final String LOADED_KEY = "loaded";

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.SHOOTER;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (this.isCharged(stack)) {
            CrossbowItem.shootAll(world, user, hand, stack, getSpeed(stack), 1.0f);
            CrossbowItem.setCharged(stack, false);
            return TypedActionResult.consume(stack);
        }

        if (!user.getAmmunition(this).isEmpty()) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!this.chargeable) {
            return;
        }
        this.tryLoad(stack, world, user, this.getPullProgress(stack, remainingUseTicks));
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float pullProgress = this.getPullProgress(stack, remainingUseTicks);
        if (this.chargeable) {
            this.charge(stack, world, user, pullProgress);
            return;
        }
        if (!(user instanceof PlayerEntity player)) {
            return;
        }
        this.shoot(stack, world, player, pullProgress);
    }

    public boolean isHeldAmmunition(ItemStack stack) {
        return stack.isIn(this.heldAmmunition);
    }

    public boolean isAmmunition(ItemStack stack) {
        return stack.isIn(this.ammunition);
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

    private void shoot(ItemStack stack, World world, PlayerEntity player, float pullProgress) {
        if (pullProgress < 0.1f) {
            return;
        }
        ItemStack ammunition = player.getAmmunition(this);
        if (ammunition.isEmpty()) {
            return;
        }
        boolean disallowPickup = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 && ammunition.isOf(ItemKeys.ARROW);
        if (!world.isClient()) {
            this.createProjectile(stack, ammunition, world, player, pullProgress, disallowPickup);
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

    private void createProjectile(ItemStack stack, ItemStack ammunition, World world, PlayerEntity player, float pullProgress, boolean disallowPickup) {
        Optional<Entity> optionalEntity = ammunition.getComponent(ItemComponentTypes.PROJECTILE)
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

            stack.damage(1, player, player.getActiveHand());
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

    private float getPullProgress(ItemStack stack, int remainingUseTicks) {
        int useTicks = stack.getMaxUseTime() - remainingUseTicks;
        if (this.chargeable) {
            float progress = (float)useTicks / getPullTime(stack);
            return Math.min(progress, 1.0f);
        }
        return BowItem.getPullProgress(useTicks);
    }

    private static int getPullTime(ItemStack stack) {
        int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return stack.getMaxUseTime() - 5 * quickChargeLevel + 3;
    }

    private static float getSpeed(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.PROJECTILE)
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

    private boolean isCharged(ItemStack stack) {
        return this.chargeable && CrossbowItem.isCharged(stack);
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
