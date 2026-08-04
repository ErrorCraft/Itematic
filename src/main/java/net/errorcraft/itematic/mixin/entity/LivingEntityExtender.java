package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.WeaponAttackDamageDataComponent;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ConsumableItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.Holder;
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
    public abstract boolean isHolding(Predicate<ItemStack> predicate);

    @Shadow
    public abstract void startUsingItem(InteractionHand hand);

    @Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract AttributeMap getAttributes();

    @Shadow
    public abstract double getAttributeBaseValue(Holder<Attribute> attribute);

    @Shadow
    public abstract InteractionHand getUsedItemHand();

    @Unique
    private int itemUsedTicks;

    public LivingEntityExtender(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(
        method = "getEquipmentSlotForItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehaviorEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.setReturnValue(EquipmentSlot.MAINHAND);
        }
    }

    @Inject(
        method = "onEquipItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        if (!newStack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.cancel();
        }
    }

    @Inject(
        method = "getProjectile",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getAmmunitionUseItemComponent(ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (stack.itematic$hasBehavior(ItemComponentTypes.SHOOTER)) {
            info.setReturnValue(this.itematic$getAmmunition(stack));
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
    private boolean isOfForSkeletonSkullUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SKELETON_SKULL);
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
    private boolean isOfForZombieHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ZOMBIE_HEAD);
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
    private boolean isOfForCreeperHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CREEPER_HEAD);
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
    private boolean isOfForPiglinHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.PIGLIN_HEAD);
    }

    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> T getDeathProtectionDataComponentUseEventListenerCheck(ItemStack instance, DataComponentType<T> type) {
        if (instance.itematic$hasEventListener(ItemEvents.BEFORE_DEATH_HOLDER)) {
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
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local(ordinal = 0) ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getItemHolder());
    }

    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/DeathProtection;applyEffects(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V"
        )
    )
    private void invokeBeforeDeathHolderEvent(DeathProtection instance, ItemStack stack, LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel serverWorld)) {
            return;
        }

        ActionContext context = ActionContext.builder(serverWorld)
            .stackExchanger(entity, stack)
            .add(LootContextParams.THIS_ENTITY, entity)
            .add(LootContextParams.ORIGIN, entity.position())
            .add(LootContextParams.TOOL, stack)
            .build();
        stack.itematic$invokeEvent(ItemEvents.BEFORE_DEATH_HOLDER, context);
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
        this.itemUsedTicks = 0;
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
        this.itemUsedTicks = 0;
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
        this.itemUsedTicks = 0;
    }

    @ModifyReturnValue(
        method = "isBlocking",
        at = @At("TAIL")
    )
    private boolean checkForUsedTicksDirectlyInsteadOfCalculating(boolean original) {
        return this.itemUsedTicks >= 5;
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
        this.itemUsedTicks++;
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
        if (original == -1) {
            return 0;
        }

        return original;
    }

    @Inject(
        method = "spawnItemParticles",
        at = @At("HEAD"),
        cancellable = true
    )
    private void shouldSpawnParticles(ItemStack stack, int count, CallbackInfo info) {
        if (!this.useItem.itematic$getBehavior(ItemComponentTypes.CONSUMABLE).map(ConsumableItemComponent::hasConsumeParticles).orElse(false)) {
            info.cancel();
        }
    }

    @Redirect(
        method = "canGlideUsing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private static boolean containsGliderUseItemComponent(ItemStack instance, DataComponentType<Unit> type) {
        return instance.itematic$getBehavior(ItemComponentTypes.GLIDER)
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
    private static boolean doNotCheckBreakOnUse(ItemStack instance) {
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
    private void checkPresenceEquipmentBehaviorBoolean(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
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
    private Object checkPresenceEquipmentBehavior(ItemStack instance, DataComponentType<Equippable> type, Operation<Object> original) {
        if (!instance.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
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
        return this.level().itematic$createStack(ItemKeys.WITHER_ROSE);
    }

    @ModifyReturnValue(
        method = "getUseItemRemainingTicks",
        at = @At("RETURN")
    )
    private int useMaxValueWhenUseDurationIsIndefinite(int original) {
        if (original == -1) {
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
        return this.itemUsedTicks;
    }

    @Override
    public boolean itematic$isHolding(ResourceKey<Item> key) {
        return this.isHolding(stack -> stack.itematic$isOf(key));
    }

    @Override
    public void itematic$startUsingHand(InteractionHand hand, int ticks) {
        ItemStack stack = this.getItemInHand(hand);
        if (stack.isEmpty() || this.isUsingItem()) {
            return;
        }
        this.startUsingItem(hand);
        this.useItemRemaining = ticks;
    }

    @Override
    public int itematic$itemUsedTicks() {
        return this.itemUsedTicks;
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
    private Double getBaseAttackDamage(ItemStack stack) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.WEAPON)) {
            return null;
        }

        WeaponAttackDamageDataComponent weaponAttackDamage = stack.get(ItematicDataComponentTypes.WEAPON_ATTACK_DAMAGE);
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
