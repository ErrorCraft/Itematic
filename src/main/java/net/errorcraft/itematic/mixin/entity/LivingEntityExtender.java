package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FoodItemComponent;
import net.errorcraft.itematic.item.component.components.UseDurationItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityExtender extends Entity implements LivingEntityAccess {
    public LivingEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

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

    @Shadow
    public abstract void equipStack(EquipmentSlot slow, ItemStack stack);

    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isFood()Z"
        )
    )
    private boolean eatFoodAssumeExistingItemComponent(ItemStack instance) {
        return true;
    }

    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
        )
    )
    private void eatFoodDoNotDecrementItemStack(ItemStack instance, int amount) {}

    @Inject(
        method = "applyFoodEffects",
        at = @At("HEAD"),
        cancellable = true
    )
    private void applyFoodEffectsUseItemComponent(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo info) {
        info.cancel();
        if (world.isClient()) {
            return;
        }
        stack.itematic$getComponent(ItemComponentTypes.FOOD)
            .map(FoodItemComponent::effects)
            .ifPresent(effects -> {
                for (FoodItemComponent.Effect effect : effects) {
                    effect.tryApply(targetEntity, world.random);
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
    private static Equipment getPreferredEquipmentSlotFromStackUseItemComponent(ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.EQUIPMENT).orElse(null);
    }

    @Redirect(
        method = "onEquipStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Equipment;fromStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/Equipment;"
        )
    )
    private Equipment onEquipStackFromStackUseItemComponent(ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.EQUIPMENT).orElse(null);
    }

    @Inject(
        method = "getProjectileType",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getProjectileTypeUseItemComponent(ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
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
    private boolean getAttackDistanceScalingFactorIsOfForSkeletonSkullUseRegistryKeyCheck(ItemStack instance, Item item) {
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
    private boolean getAttackDistanceScalingFactorIsOfForZombieHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
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
    private boolean getAttackDistanceScalingFactorIsOfForCreeperHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
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
    private boolean getAttackDistanceScalingFactorIsOfForPiglinHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
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
    public boolean itematic$isHolding(ItemStack stack) {
        return this.isHolding(s -> s == stack);
    }

    @Override
    public void itematic$eatFood(World world, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        this.eatFood(world, stack);
        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, resultStackConsumer, this.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, this);
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
}
