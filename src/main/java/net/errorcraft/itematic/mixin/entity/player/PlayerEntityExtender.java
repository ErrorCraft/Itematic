package net.errorcraft.itematic.mixin.entity.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.ItemListDataComponent;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityExtender extends LivingEntity implements LivingEntityAccess {
    @Shadow
    @Final
    PlayerInventory inventory;

    @Shadow
    @Final
    private PlayerAbilities abilities;

    @Shadow
    public abstract PlayerInventory getInventory();

    protected PlayerEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;isEquipped(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isEquippedForTurtleHelmetUseRegistryKeyCheck(PlayerEntity instance, Item item) {
        return this.isEquipped(ItemKeys.TURTLE_HELMET);
    }

    @Redirect(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/entity/attribute/EntityAttributes;ATTACK_DAMAGE:Lnet/minecraft/registry/entry/RegistryEntry;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private double useCustomAttackDamage(PlayerEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    @Redirect(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private void neverSetEmptyStack(PlayerEntity instance, Hand hand, ItemStack stack) {}

    @ModifyExpressionValue(
        method = "getAttackCooldownProgressPerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private double multiplyByAttackSpeedMultiplier(double original) {
        return this.inventory.getMainHandStack().itematic$attackSpeedMultiplier() * original;
    }

    @Redirect(
        method = "isUsingSpyglass",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemComponentTypes.ZOOM);
    }

    @Override
    public boolean itematic$hasStackInInventory(ItemStack stack) {
        return this.getInventory().contains(stack);
    }

    @Override
    public ItemStack itematic$getAmmunition(ItemStack stack) {
        ItemListDataComponent heldAmmunition = stack.getOrDefault(ItematicDataComponentTypes.SHOOTER_HELD_AMMUNITION, ItemListDataComponent.DEFAULT);
        ItemStack heldStack = RangedWeaponItem.getHeldProjectile(this, heldAmmunition::isValidFor);
        if (!heldStack.isEmpty()) {
            return heldStack;
        }

        ItemListDataComponent ammunition = stack.getOrDefault(ItematicDataComponentTypes.SHOOTER_AMMUNITION, ItemListDataComponent.DEFAULT);
        for (int i = 0; i < this.inventory.size(); i++) {
            ItemStack inventoryStack = this.inventory.getStack(i);
            if (ammunition.isValidFor(inventoryStack)) {
                return inventoryStack;
            }
        }

        return this.abilities.creativeMode ? this.getWorld().itematic$createStack(ItemKeys.ARROW) : ItemStack.EMPTY;
    }

    @Unique
    @SuppressWarnings("SameParameterValue")
    private boolean isEquipped(RegistryKey<Item> item) {
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            ItemStack equippedStack = this.getEquippedStack(slot);
            if (!equippedStack.itematic$isOf(item)) {
                continue;
            }

            EquippableComponent equippable = equippedStack.get(DataComponentTypes.EQUIPPABLE);
            if (equippable != null && equippable.slot() == slot) {
                return true;
            }
        }

        return false;
    }
}
