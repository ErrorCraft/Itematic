package net.errorcraft.itematic.mixin.entity.player;

import com.mojang.authlib.GameProfile;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityExtender extends LivingEntity {
    @Shadow
    @Final
    private PlayerInventory inventory;

    @Shadow
    protected EnderChestInventory enderChestInventory;

    @Shadow
    @Final
    private PlayerAbilities abilities;

    protected PlayerEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerEntity;enderChestInventory:Lnet/minecraft/inventory/EnderChestInventory;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void setPlayer(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo info) {
        this.enderChestInventory.setPlayer((PlayerEntity)(Object) this);
    }

    @Redirect(
        method = "updateTurtleHelmet",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean updateTurtleHelmetIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.TURTLE_HELMET);
    }

    @Override
    public ItemStack getAmmunition(ShooterItemComponent itemComponent) {
        ItemStack heldStack = RangedWeaponItem.getHeldProjectile(this, itemComponent::isHeldAmmunition);
        if (!heldStack.isEmpty()) {
            return heldStack;
        }

        for (int i = 0; i < this.inventory.size(); ++i) {
            ItemStack stack = this.inventory.getStack(i);
            if (itemComponent.isAmmunition(stack)) {
                return stack;
            }
        }

        return this.abilities.creativeMode ? new ItemStack(this.getWorld().getItem(ItemKeys.ARROW)) : ItemStack.EMPTY;
    }

    @Redirect(
        method = "checkFallFlying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForElytraUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.ELYTRA);
    }

    @Redirect(
        method = "damageShield",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForShieldUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.SHIELD);
    }

    @ModifyArg(
        method = "disableShield",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"
        )
    )
    private Item setCooldownForShieldUseDynamicRegistry(Item item) {
        return this.getWorld().getItem(ItemKeys.SHIELD).value();
    }
}
