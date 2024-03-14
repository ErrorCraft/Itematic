package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FoodItemComponent;
import net.errorcraft.itematic.item.component.components.LifeSavingItemComponent;
import net.errorcraft.itematic.item.component.components.UseDurationItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityExtender extends Entity implements LivingEntityAccess {
    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    protected int itemUseTimeLeft;

    @Shadow
    public abstract boolean isHolding(Predicate<ItemStack> predicate);

    @Shadow
    public abstract Hand getActiveHand();

    @Shadow
    public abstract void setCurrentHand(Hand hand);

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract ItemStack eatFood(World world, ItemStack stack);

    @Unique
    private int itemUsedTicks;

    public LivingEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isFood()Z"
        )
    )
    private boolean doNotCheckForFood(ItemStack instance) {
        return true;
    }

    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"
        )
    )
    private void doNotDecrementItemStack(ItemStack instance, int amount, LivingEntity entity) {}

    @Inject(
        method = "applyFoodEffects",
        at = @At("HEAD"),
        cancellable = true
    )
    private void applyEffectsUseItemComponent(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo info) {
        info.cancel();
        if (world.isClient()) {
            return;
        }
        stack.itematic$getComponent(ItemComponentTypes.FOOD)
            .map(FoodItemComponent::effects)
            .ifPresent(effects -> {
                Random random = world.getRandom();
                for (FoodItemComponent.Effect effect : effects) {
                    effect.tryApply(targetEntity, random);
                }
            });
    }

    @Redirect(
        method = "getPreferredEquipmentSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Equipment;fromStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/Equipment;"
        )
    )
    private static Equipment equipmentFromStackUseItemComponentStatic(ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.EQUIPMENT).orElse(null);
    }

    @Redirect(
        method = "onEquipStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Equipment;fromStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/Equipment;"
        )
    )
    private Equipment equipmentFromStackUseItemComponent(ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.EQUIPMENT).orElse(null);
    }

    @Inject(
        method = "getProjectileType",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getAmmunitionUseItemComponent(ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        stack.itematic$getComponent(ItemComponentTypes.SHOOTER)
            .ifPresent(component -> info.setReturnValue(this.itematic$getAmmunition(component)));
    }

    @Redirect(
        method = "getAttackDistanceScalingFactor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;SKELETON_SKULL:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForSkeletonSkullUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SKELETON_SKULL);
    }

    @Redirect(
        method = "getAttackDistanceScalingFactor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;ZOMBIE_HEAD:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForZombieHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ZOMBIE_HEAD);
    }

    @Redirect(
        method = "getAttackDistanceScalingFactor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CREEPER_HEAD:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForCreeperHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CREEPER_HEAD);
    }

    @Redirect(
        method = "getAttackDistanceScalingFactor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;PIGLIN_HEAD:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CREEPER_HEAD:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForPiglinHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.PIGLIN_HEAD);
    }

    @Redirect(
        method = "getVisibilityBoundingBox",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForDragonHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.DRAGON_HEAD);
    }

    @Redirect(
        method = "tryUseTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForTotemOfUndyingUseItemComponent(ItemStack instance, Item item, @Share("lifeSavingItemComponent") LocalRef<LifeSavingItemComponent> lifeSavingItemComponent) {
        Optional<LifeSavingItemComponent> optionalComponent = instance.itematic$getComponent(ItemComponentTypes.LIFE_SAVING);
        optionalComponent.ifPresent(lifeSavingItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "tryUseTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local(ordinal = 0) ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
    }

    @Inject(
        method = "tryUseTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z",
            shift = At.Shift.AFTER
        )
    )
    private void addEffectsFromLifeSavingItemComponent(DamageSource source, CallbackInfoReturnable<Boolean> info, @Share("lifeSavingItemComponent") LocalRef<LifeSavingItemComponent> lifeSavingItemComponent) {
        lifeSavingItemComponent.get().apply((LivingEntity)(Object) this);
    }

    @Redirect(
        method = "tryUseTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"
        )
    )
    private boolean doNotAddStatusEffects(LivingEntity instance, StatusEffectInstance effect) {
        return false;
    }

    @Inject(
        method = "setCurrentHand",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/LivingEntity;itemUseTimeLeft:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void resetUseTime(Hand hand, CallbackInfo info) {
        this.itemUsedTicks = 0;
    }

    @Inject(
        method = "onTrackedDataSet",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/LivingEntity;itemUseTimeLeft:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void resetUseTime(TrackedData<?> data, CallbackInfo info) {
        this.itemUsedTicks = 0;
    }

    @Inject(
        method = "clearActiveItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/LivingEntity;itemUseTimeLeft:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void resetUseTime(CallbackInfo info) {
        this.itemUsedTicks = 0;
    }

    @Inject(
        method = "tickItemStackUsage",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/LivingEntity;itemUseTimeLeft:I",
            opcode = Opcodes.GETFIELD
        )
    )
    private void incrementUseTime(ItemStack stack, CallbackInfo info) {
        this.itemUsedTicks++;
    }

    @ModifyExpressionValue(
        method = "tickItemStackUsage",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/LivingEntity;itemUseTimeLeft:I",
            opcode = Opcodes.GETFIELD
        )
    )
    private int keepAtConstantWhenUseDurationIsIndefinite(int original) {
        if (original == -1) {
            return 0;
        }
        return original;
    }

    @ModifyVariable(
        method = "shouldSpawnConsumptionEffects",
        at = @At("LOAD")
    )
    private boolean shouldSpawnConsumptionEffectsUseItemComponent(boolean value) {
        return this.activeItemStack.itematic$getComponent(ItemComponentTypes.USE_DURATION)
            .map(UseDurationItemComponent::ticks)
            .map(ticks -> value || ticks <= 16)
            .orElse(false);
    }

    @Redirect(
        method = "tickFallFlying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForElytraUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ELYTRA);
    }

    @Redirect(
        method = "onKilledBy",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForWitherRoseUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.WITHER_ROSE);
    }

    @ModifyReturnValue(
        method = "getItemUseTime",
        at = @At(
            value = "RETURN",
            ordinal = 0
        )
    )
    private int getItemUseTimeUseField(int original) {
        return this.itemUsedTicks;
    }

    /**
     * @author ErrorCraft
     * @reason Uses an item tag check instead of an instanceof check.
     */
    @Overwrite
    public boolean disablesShield() {
        return this.getMainHandStack().isIn(ItemTags.AXES);
    }

    @Override
    public boolean itematic$isHolding(RegistryKey<Item> key) {
        return this.isHolding(stack -> stack.itematic$isOf(key));
    }

    @Override
    public void itematic$eatFood(World world, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        this.eatFood(world, stack);
        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, this.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, this)
                .build();
            stack.itematic$invokeEvent(ItemEvents.EAT_ITEM, context);
        }
    }

    @Override
    public void itematic$startUsingHand(Hand hand, int ticks) {
        ItemStack stack = this.getStackInHand(hand);
        if (stack.isEmpty() || this.isUsingItem()) {
            return;
        }
        this.setCurrentHand(hand);
        this.itemUseTimeLeft = ticks;
    }

    @Override
    public int itematic$itemUsedTicks() {
        return this.itemUsedTicks;
    }
}
