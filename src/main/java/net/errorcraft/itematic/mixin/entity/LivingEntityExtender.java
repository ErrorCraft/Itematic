package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FoodItemComponent;
import net.errorcraft.itematic.item.component.components.UseDurationItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityExtender extends Entity implements LivingEntityAccess {
    public LivingEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    public abstract boolean isHolding(Predicate<ItemStack> predicate);

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
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;emitGameEvent(Lnet/minecraft/world/event/GameEvent;)V",
            shift = At.Shift.AFTER
        )
    )
    private void eatFoodInvokeEatItemEvent(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (world instanceof ServerWorld serverWorld) {
            ActionContext.Builder builder = ActionContext.builder(serverWorld, stack)
                .entityPosition(ActionContextParameter.THIS, this);
            stack.invokeEvent(ItemEvents.EAT_ITEM, builder);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    private void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity) {
        if (world.isClient()) {
            return;
        }
        Optional<FoodItemComponent> component = stack.getComponent(ItemComponentTypes.FOOD);
        if (component.isEmpty()) {
            return;
        }
        for (FoodItemComponent.Effect effect : component.get().effects()) {
            effect.tryApply(targetEntity, world.random);
        }
    }

    @Redirect(
        method = "getPreferredEquipmentSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Equipment;fromStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/Equipment;"
        )
    )
    private static Equipment getPreferredEquipmentSlotFromStackUseItemComponent(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.EQUIPMENT).orElse(null);
    }

    @Redirect(
        method = "onEquipStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Equipment;fromStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/Equipment;"
        )
    )
    private Equipment onEquipStackFromStackUseItemComponent(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.EQUIPMENT).orElse(null);
    }

    @Inject(
        method = "onEquipStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        )
    )
    private void onEquipStackPlaySoundInvokeEquipItemEvent(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        ActionContext.Builder builder = ActionContext.builder((ServerWorld) this.getWorld(), newStack)
            .entityPosition(ActionContextParameter.THIS, this);
        newStack.invokeEvent(ItemEvents.EQUIP_ITEM, builder);
    }

    @Inject(
        method = "getProjectileType",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getProjectileTypeUseItemComponent(ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        stack.getComponent(ItemComponentTypes.SHOOTER)
            .ifPresent(component -> info.setReturnValue(this.getAmmunition(component)));
    }

    @Override
    public boolean isHolding(RegistryKey<Item> key) {
        return this.isHolding(stack -> stack.isOf(key));
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
        return instance.isOf(ItemKeys.SKELETON_SKULL);
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
        return instance.isOf(ItemKeys.ZOMBIE_HEAD);
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
        return instance.isOf(ItemKeys.CREEPER_HEAD);
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
        return instance.isOf(ItemKeys.PIGLIN_HEAD);
    }

    @Redirect(
        method = "getVisibilityBoundingBox",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean getVisibilityBoundingBoxIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.DRAGON_HEAD);
    }

    @Redirect(
        method = "shouldSpawnConsumptionEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"
        )
    )
    private FoodComponent shouldSpawnConsumptionEffectsGetFoodComponentReturnNull(Item instance) {
        return null;
    }

    @ModifyVariable(
        method = "shouldSpawnConsumptionEffects",
        at = @At(value = "LOAD")
    )
    private boolean shouldSpawnConsumptionEffectsUseItemComponent(boolean value) {
        return this.activeItemStack.getComponent(ItemComponentTypes.USE_DURATION)
            .map(UseDurationItemComponent::ticks)
            .map(ticks -> ticks <= 16)
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
        return instance.isOf(ItemKeys.ELYTRA);
    }
}
