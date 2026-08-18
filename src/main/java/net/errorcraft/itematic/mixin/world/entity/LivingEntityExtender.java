package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.entity.LivingEntityAccess;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.weapon.melee.WeaponAttackDamage;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityExtender extends Entity implements LivingEntityAccess {
    @Shadow
    protected ItemStack useItem;

    @Shadow
    protected int useItemRemaining;

    @Shadow
    public abstract double getAttributeBaseValue(Holder<Attribute> attribute);

    @Shadow
    public abstract AttributeMap getAttributes();

    @Shadow
    public abstract ItemStack getMainHandItem();

    @Shadow
    public abstract boolean isHolding(Predicate<ItemStack> predicate);

    @Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract InteractionHand getUsedItemHand();

    @Shadow
    public abstract void startUsingItem(InteractionHand hand);

    @Unique
    private int usedItemTicks;

    public LivingEntityExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapMethod(
        method = "getEquipmentSlotForItem"
    )
    private EquipmentSlot checkEquipmentItemBehavior(ItemStack stack, Operation<EquipmentSlot> original) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return EquipmentSlot.MAINHAND;
        }

        return original.call(stack);
    }

    @Inject(
        method = "onEquipItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void checkEquipmentItemBehavior(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        if (!newStack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            info.cancel();
        }
    }

    @Redirect(
        method = "getVisibilityPercent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;SKELETON_SKULL:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isSkeletonSkullCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.SKELETON_SKULL);
    }

    @Redirect(
        method = "getVisibilityPercent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;ZOMBIE_HEAD:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isZombieHeadCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.ZOMBIE_HEAD);
    }

    @Redirect(
        method = "getVisibilityPercent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CREEPER_HEAD:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isCreeperHeadCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.CREEPER_HEAD);
    }

    @Redirect(
        method = "getVisibilityPercent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;PIGLIN_HEAD:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CREEPER_HEAD:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isPiglinHeadCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.PIGLIN_HEAD);
    }

    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    @Nullable
    @SuppressWarnings("unchecked")
    private <T> T getDeathProtectionDataComponentCheckEventListener(ItemStack instance, DataComponentType<T> type) {
        if (instance.itematic$hasEventListener(ItemEvent.BEFORE_DEATH_HOLDER)) {
            return (T) DeathProtection.TOTEM_OF_UNDYING;
        }

        return null;
    }

    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T key, @Local(ordinal = 0) ItemStack stack) {
        return instance.itematic$get(stack.getItemHolder());
    }

    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/DeathProtection;applyEffects(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V"
        )
    )
    private void invokeBeforeDeathHolderEvent(DeathProtection instance, ItemStack stack, LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        ActionContext context = ActionContext.builder(level)
            .stackExchanger(entity, stack)
            .add(LootContextParams.THIS_ENTITY, entity)
            .add(LootContextParams.ORIGIN, entity.position())
            .add(LootContextParams.TOOL, stack)
            .build();
        stack.itematic$invokeEvent(ItemEvent.BEFORE_DEATH_HOLDER, context);
    }

    @Inject(
        method = "startUsingItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;useItemRemaining:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void resetUseTime(InteractionHand hand, CallbackInfo info) {
        this.usedItemTicks = 0;
    }

    @Inject(
        method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;useItemRemaining:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void resetUseTime(EntityDataAccessor<?> data, CallbackInfo info) {
        this.usedItemTicks = 0;
    }

    @Inject(
        method = "stopUsingItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;useItemRemaining:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void resetUseTime(CallbackInfo info) {
        this.usedItemTicks = 0;
    }

    @ModifyReturnValue(
        method = "isBlocking",
        at = @At("TAIL")
    )
    private boolean checkForUsedTicksDirectlyInsteadOfCalculating(boolean original) {
        return this.usedItemTicks >= 5;
    }

    @Inject(
        method = "updateUsingItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;useItemRemaining:I",
            opcode = Opcodes.GETFIELD
        )
    )
    private void incrementUseTime(ItemStack stack, CallbackInfo info) {
        this.usedItemTicks++;
    }

    @ModifyExpressionValue(
        method = "updateUsingItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;useItemRemaining:I",
            opcode = Opcodes.GETFIELD
        )
    )
    private int keepAtConstantWhenUseDurationIsIndefinite(int original) {
        if (original == UseDuration.INDEFINITE) {
            return 0;
        }

        return original;
    }

    @Redirect(
        method = "canGlideUsing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private static boolean hasGliderUseItemBehavior(ItemStack instance, DataComponentType<Unit> type) {
        return instance.itematic$getBehavior(ItemBehaviorType.GLIDER)
            .map(glider -> glider.canUse(instance))
            .orElse(false);
    }

    @Redirect(
        method = "canGlideUsing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;nextDamageWillBreak()Z"
        )
    )
    private static boolean doNotCheckBrokenGlider(ItemStack instance) {
        return false;
    }

    @Inject(
        method = "canEquipWithDispenser",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void checkEquipmentItemBehavior(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "canEquipWithDispenser",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/Equippable;dispensable()Z"
        )
    )
    private boolean dispensableAlwaysTrue(Equippable instance) {
        return true;
    }

    @WrapOperation(
        method = "isEquippableInSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    @Nullable
    private Object checkEquipmentItemBehavior(ItemStack instance, DataComponentType<Equippable> type, Operation<Object> original) {
        if (!instance.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return null;
        }

        return original.call(instance, type);
    }

    @Redirect(
        method = "createWitherRose",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForWitherRoseUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.WITHER_ROSE);
    }

    @ModifyReturnValue(
        method = "getUseItemRemainingTicks",
        at = @At("RETURN")
    )
    private int useMaxValueWhenUseDurationIsIndefinite(int original) {
        if (original == UseDuration.INDEFINITE) {
            return Integer.MAX_VALUE;
        }

        return original;
    }

    @ModifyReturnValue(
        method = "getTicksUsingItem()I",
        at = @At(
            value = "RETURN",
            ordinal = 0
        )
    )
    private int getItemUseTimeUseField(int original) {
        return this.usedItemTicks;
    }

    @Override
    public boolean itematic$isHolding(ResourceKey<Item> key) {
        return this.isHolding(stack -> stack.itematic$is(key));
    }

    @Override
    public ItemStack itematic$getHeldItem(HolderSet<Item> items) {
        ItemStack offHandStack = this.getItemInHand(InteractionHand.OFF_HAND);
        if (offHandStack.is(items)) {
            return offHandStack;
        }

        ItemStack mainHandStack = this.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHandStack.is(items)) {
            return mainHandStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack itematic$getAmmunition(ItemStack stack) {
        HolderSet<Item> shooterHeldAmmunition = stack.get(ItematicDataComponents.SHOOTER_HELD_AMMUNITION);
        if (shooterHeldAmmunition == null) {
            return ItemStack.EMPTY;
        }

        return this.itematic$getHeldItem(shooterHeldAmmunition);
    }

    @Override
    public void itematic$startUsingItem(InteractionHand hand, int ticks) {
        ItemStack stack = this.getItemInHand(hand);
        if (stack.isEmpty() || this.isUsingItem()) {
            return;
        }
        this.startUsingItem(hand);
        this.useItemRemaining = ticks;
    }

    @Override
    public int itematic$usedItemTicks() {
        return this.usedItemTicks;
    }

    @Override
    public double itematic$getAttackDamage() {
        InteractionHand usedHand = this.getUsedItemHand();
        Double baseAttackDamage = this.getBaseAttackDamage(this.getItemInHand(usedHand));
        return this.getAttributes().itematic$getValue(Attributes.ATTACK_DAMAGE, baseAttackDamage);
    }

    @Override
    public double itematic$getBaseAttackDamage() {
        InteractionHand usedHand = this.getUsedItemHand();
        Double baseAttackDamage = this.getBaseAttackDamage(this.getItemInHand(usedHand));
        if (baseAttackDamage != null) {
            return baseAttackDamage;
        }

        return this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
    }

    @Unique
    @Nullable
    private Double getBaseAttackDamage(ItemStack stack) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.WEAPON)) {
            return null;
        }

        WeaponAttackDamage weaponAttackDamage = stack.get(ItematicDataComponents.WEAPON_ATTACK_DAMAGE);
        if (weaponAttackDamage == null) {
            return null;
        }

        double damage = weaponAttackDamage.getDamage(stack, this);
        if (weaponAttackDamage.shouldAddBase(stack, this)) {
            return damage + this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        }

        return damage;
    }
}
