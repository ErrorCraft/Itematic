package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EquipmentItemComponent;
import net.errorcraft.itematic.item.component.components.FoodItemComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityExtender implements LivingEntityAccess {
    @Shadow
    public abstract boolean isHolding(Predicate<ItemStack> predicate);

    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isFood()Z"
        )
    )
    private boolean assumeExistingItemComponent(ItemStack instance) {
        return true;
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

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public static EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.EQUIPMENT).map(EquipmentItemComponent::slot).orElse(EquipmentSlot.MAINHAND);
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
}
