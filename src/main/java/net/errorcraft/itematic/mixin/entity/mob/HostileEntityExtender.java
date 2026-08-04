package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.ItemListDataComponent;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(Monster.class)
public class HostileEntityExtender extends PathfinderMob implements LivingEntityAccess {
    protected HostileEntityExtender(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyConstant(
        method = "getProjectile",
        constant = @Constant(
            classValue = ProjectileWeaponItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfRangedWeaponItemUseItemComponent(Object reference, Class<ProjectileWeaponItem> clazz, @Local(argsOnly = true) ItemStack itemStack, @Share("heldAmmunitionDataComponent") LocalRef<ItemListDataComponent> heldAmmunitionDataComponentReference) {
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
        method = "getProjectile",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;",
            ordinal = 1
        )
    )
    private Item castToRangedWeaponItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "getProjectile",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getSupportedHeldProjectiles()Ljava/util/function/Predicate;"
        )
    )
    private Predicate<ItemStack> getHeldProjectilesUseItemComponent(ProjectileWeaponItem instance, @Share("heldAmmunitionDataComponent") LocalRef<ItemListDataComponent> heldAmmunitionDataComponent) {
        return heldAmmunitionDataComponent.get()::isValidFor;
    }

    @Redirect(
        method = "getProjectile",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForArrowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.ARROW);
    }

    @Override
    public ItemStack itematic$getAmmunition(ItemStack stack) {
        ItemListDataComponent heldAmmunition = stack.getOrDefault(ItematicDataComponentTypes.SHOOTER_HELD_AMMUNITION, ItemListDataComponent.DEFAULT);
        ItemStack heldStack = ProjectileWeaponItem.getHeldProjectile(this, heldAmmunition::isValidFor);
        if (!heldStack.isEmpty()) {
            return heldStack;
        }

        return this.level().itematic$createStack(ItemKeys.ARROW);
    }
}
