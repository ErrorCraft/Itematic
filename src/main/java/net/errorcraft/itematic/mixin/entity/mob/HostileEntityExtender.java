package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.ItemListDataComponent;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(HostileEntity.class)
public class HostileEntityExtender extends PathAwareEntity implements LivingEntityAccess {
    protected HostileEntityExtender(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(
        method = "getProjectileType",
        constant = @Constant(
            classValue = RangedWeaponItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfRangedWeaponItemUseItemComponent(Object reference, Class<RangedWeaponItem> clazz, @Local(argsOnly = true) ItemStack itemStack, @Share("heldAmmunitionDataComponent") LocalRef<ItemListDataComponent> heldAmmunitionDataComponentReference) {
        if (!itemStack.itematic$hasBehavior(ItemComponentTypes.SHOOTER)) {
            return false;
        }
        ItemListDataComponent heldAmmunitionDataComponent = itemStack.get(ItematicDataComponentTypes.SHOOTER_HELD_AMMUNITION);
        if (heldAmmunitionDataComponent == null) {
            return false;
        }
        heldAmmunitionDataComponentReference.set(heldAmmunitionDataComponent);
        return true;
    }

    @Redirect(
        method = "getProjectileType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 1
        )
    )
    private Item castToRangedWeaponItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "getProjectileType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"
        )
    )
    private Predicate<ItemStack> getHeldProjectilesUseItemComponent(RangedWeaponItem instance, @Share("heldAmmunitionDataComponent") LocalRef<ItemListDataComponent> heldAmmunitionDataComponent) {
        return heldAmmunitionDataComponent.get()::isValidFor;
    }

    @Redirect(
        method = "getProjectileType",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForArrowUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.ARROW);
    }

    @Override
    public ItemStack itematic$getAmmunition(ItemStack stack) {
        ItemListDataComponent heldAmmunition = stack.getOrDefault(ItematicDataComponentTypes.SHOOTER_HELD_AMMUNITION, ItemListDataComponent.DEFAULT);
        ItemStack heldStack = RangedWeaponItem.getHeldProjectile(this, heldAmmunition::isValidFor);
        if (!heldStack.isEmpty()) {
            return heldStack;
        }

        return this.getWorld().itematic$createStack(ItemKeys.ARROW);
    }
}
