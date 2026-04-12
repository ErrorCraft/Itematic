package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.access.entity.attribute.AttributeContainerAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.WeaponAttackDamageDataComponent;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ConsumableItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
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
    protected ItemStack activeItemStack;

    @Shadow
    protected int itemUseTimeLeft;

    @Shadow
    public abstract boolean isHolding(Predicate<ItemStack> predicate);

    @Shadow
    public abstract void setCurrentHand(Hand hand);

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract AttributeContainer getAttributes();

    @Shadow
    public abstract double getAttributeBaseValue(RegistryEntry<EntityAttribute> attribute);

    @Unique
    private int itemUsedTicks;

    public LivingEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "getPreferredEquipmentSlot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehaviorEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.setReturnValue(EquipmentSlot.MAINHAND);
        }
    }

    @Inject(
        method = "onEquipStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        if (!newStack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.cancel();
        }
    }

    @Inject(
        method = "getProjectileType",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getAmmunitionUseItemComponent(ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (stack.itematic$hasBehavior(ItemComponentTypes.SHOOTER)) {
            info.setReturnValue(this.itematic$getAmmunition(stack));
        }
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
        method = "tryUseDeathProtector",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> T getDeathProtectionDataComponentUseEventListenerCheck(ItemStack instance, ComponentType<T> type) {
        if (instance.itematic$hasEventListener(ItemEvents.BEFORE_DEATH_HOLDER)) {
            return (T) DeathProtectionComponent.TOTEM_OF_UNDYING;
        }

        return null;
    }

    @Redirect(
        method = "tryUseDeathProtector",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local(ordinal = 0) ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
    }

    @Redirect(
        method = "tryUseDeathProtector",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/DeathProtectionComponent;applyDeathEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)V"
        )
    )
    private void invokeBeforeDeathHolderEvent(DeathProtectionComponent instance, ItemStack stack, LivingEntity entity) {
        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        ActionContext context = ActionContext.builder(serverWorld)
            .stackExchanger(entity, stack)
            .add(LootContextParameters.THIS_ENTITY, entity)
            .add(LootContextParameters.ORIGIN, entity.getPos())
            .add(LootContextParameters.TOOL, stack)
            .build();
        stack.itematic$invokeEvent(ItemEvents.BEFORE_DEATH_HOLDER, context);
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

    @ModifyReturnValue(
        method = "isBlocking",
        at = @At("TAIL")
    )
    private boolean checkForUsedTicksDirectlyInsteadOfCalculating(boolean original) {
        return this.itemUsedTicks >= 5;
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

    @Inject(
        method = "spawnItemParticles",
        at = @At("HEAD"),
        cancellable = true
    )
    private void shouldSpawnParticles(ItemStack stack, int count, CallbackInfo info) {
        if (!this.activeItemStack.itematic$getBehavior(ItemComponentTypes.CONSUMABLE).map(ConsumableItemComponent::hasConsumeParticles).orElse(false)) {
            info.cancel();
        }
    }

    @Redirect(
        method = "canGlideWith",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    private static boolean containsGliderUseItemComponent(ItemStack instance, ComponentType<Unit> type) {
        return instance.itematic$getBehavior(ItemComponentTypes.GLIDER)
            .map(glider -> glider.canUse(instance))
            .orElse(false);
    }

    @Redirect(
        method = "canGlideWith",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;willBreakNextUse()Z"
        )
    )
    private static boolean doNotCheckBreakOnUse(ItemStack instance) {
        return false;
    }

    @Inject(
        method = "canEquipFromDispenser",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void checkPresenceEquipmentBehaviorBoolean(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "canEquipFromDispenser",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/EquippableComponent;dispensable()Z"
        )
    )
    private boolean dispensableAlwaysTrue(EquippableComponent instance) {
        return true;
    }

    @WrapOperation(
        method = "canEquip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"
        )
    )
    private Object checkPresenceEquipmentBehavior(ItemStack instance, ComponentType<EquippableComponent> type, Operation<Object> original) {
        if (!instance.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            return null;
        }

        return original.call(instance, type);
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
        method = "getItemUseTimeLeft",
        at = @At("RETURN")
    )
    private int useMaxValueWhenUseDurationIsIndefinite(int original) {
        if (original == -1) {
            return Integer.MAX_VALUE;
        }

        return original;
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

    @Override
    public boolean itematic$isHolding(RegistryKey<Item> key) {
        return this.isHolding(stack -> stack.itematic$isOf(key));
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

    @Override
    public double itematic$getAttackDamage() {
        Double baseAttackDamage = this.getBaseAttackDamage(this.getMainHandStack());
        return ((AttributeContainerAccess) this.getAttributes())
            .itematic$getValue(EntityAttributes.ATTACK_DAMAGE, baseAttackDamage);
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
            return damage + this.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE);
        }

        return damage;
    }
}
