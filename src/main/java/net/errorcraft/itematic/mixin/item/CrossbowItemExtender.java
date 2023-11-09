package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ProjectileItemComponent;
import net.errorcraft.itematic.item.util.ShooterUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemExtender {
    @Redirect(
        method = "loadProjectiles",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
        )
    )
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static ItemStack loadProjectilesGetProjectileTypeUseItemComponent(LivingEntity instance, ItemStack stack) {
        return instance.itematic$getAmmunition(stack.itematic$getComponent(ItemComponentTypes.SHOOTER).get());
    }

    @Redirect(
        method = "loadProjectiles",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForArrowUseRegistryEntry(ItemConvertible item, LivingEntity shooter) {
        return shooter.getWorld().itematic$createStack(ItemKeys.ARROW);
    }

    @ModifyConstant(
        method = "loadProjectile",
        constant = @Constant(classValue = ArrowItem.class)
    )
    private static boolean loadProjectileInstanceOfArrowItemUseItemComponentCheck(Object reference, Class<ArrowItem> clazz) {
        return ((Item) reference).itematic$hasComponent(ItemComponentTypes.PROJECTILE);
    }

    @Redirect(
        method = "shootAll",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;getProjectiles(Lnet/minecraft/item/ItemStack;)Ljava/util/List;"
        )
    )
    private static List<ItemStack> getProjectilesUseDynamicRegistry(ItemStack crossbow, @Local World world) {
        return ShooterUtil.getLoadedAmmunition(crossbow, world.getRegistryManager());
    }

    @Redirect(
        method = "getProjectiles(Lnet/minecraft/item/ItemStack;)Ljava/util/List;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack getProjectilesFromNbtUseDynamicRegistry(NbtCompound nbt) {
        return ItemStackUtil.readFromNbt(nbt, ShooterUtil.registryManager());
    }

    @Redirect(
        method = "shoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForFireworkRocketUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(ProjectileItemComponent::entity)
            .map(RegistryEntry::value)
            .map(e -> e == EntityType.FIREWORK_ROCKET)
            .orElse(false);
    }

    @Redirect(
        method = "createArrow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"
        )
    )
    private static PersistentProjectileEntity createArrowCreateArrowUseItemComponent(ArrowItem instance, World world, ItemStack projectile, LivingEntity shooter) {
        Optional<ProjectileItemComponent> component = projectile.itematic$getComponent(ItemComponentTypes.PROJECTILE);
        if (component.isEmpty()) {
            return null;
        }

        Entity entity = component.get().createEntity(world, shooter, projectile, 0.0f, 1.0f);
        if (entity instanceof ArrowEntity arrowEntity) {
            arrowEntity.initFromStack(projectile);
        }

        if (entity instanceof PersistentProjectileEntity persistentProjectileEntity) {
            return persistentProjectileEntity;
        }

        world.spawnEntity(entity);
        return null;
    }

    @Inject(
        method = "createArrow",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"
        ),
        cancellable = true
    )
    private static void createArrowCreateArrowCheckNullEntity(World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow, CallbackInfoReturnable<PersistentProjectileEntity> info, @Local PersistentProjectileEntity persistentProjectileEntity) {
        if (persistentProjectileEntity == null) {
            info.setReturnValue(null);
        }
    }

    @Inject(
        method = "shoot",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/item/CrossbowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private static void shootCreateArrowCheckNullEntity(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo info, @Local ProjectileEntity projectileEntity) {
        if (projectileEntity == null) {
            info.cancel();
        }
    }

    @ModifyArg(
        method = "shoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"
        )
    )
    private static int shootDamageModifyDamageAmount(int amount, @Local(ordinal = 1) ItemStack projectile) {
        return projectile.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(ProjectileItemComponent::damage)
            .orElse(0);
    }
}
