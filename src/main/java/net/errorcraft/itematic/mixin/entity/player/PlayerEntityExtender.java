package net.errorcraft.itematic.mixin.entity.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.entity.LivingEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Player.class)
public abstract class PlayerEntityExtender extends LivingEntityExtender {
    @Shadow
    @Final
    Inventory inventory;

    @Shadow
    public abstract Inventory getInventory();

    @Shadow
    public abstract boolean isCreative();

    protected PlayerEntityExtender(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isEquipped(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isEquippedForTurtleHelmetUseRegistryKeyCheck(Player instance, Item item) {
        return this.isEquipped(ItemIds.TURTLE_HELMET);
    }

    @Redirect(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/ai/attributes/Attributes;ATTACK_DAMAGE:Lnet/minecraft/core/Holder;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private double useCustomAttackDamage(Player instance, Holder<Attribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    @Redirect(
        method = "itemAttackInteraction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private void neverSetEmptyStack(Player instance, InteractionHand hand, ItemStack stack) {}

    @ModifyExpressionValue(
        method = "getCurrentItemAttackStrengthDelay",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double multiplyByAttackSpeedMultiplier(double original) {
        return this.getMainHandItem().itematic$attackSpeedMultiplier() * original;
    }

    @Redirect(
        method = "isScoping",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfUseItemBehaviorCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemBehaviorType.ZOOM);
    }

    @Override
    public boolean itematic$hasStackInInventory(ItemStack stack) {
        return this.getInventory().contains(stack);
    }

    @Override
    public ItemStack itematic$getAmmunition(ItemStack stack) {
        ItemStack ammunition = super.itematic$getAmmunition(stack);
        if (!ammunition.isEmpty()) {
            return ammunition;
        }

        HolderSet<Item> shooterAmmunition = stack.getOrDefault(ItematicDataComponents.SHOOTER_AMMUNITION, HolderSet.empty());
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack inventoryStack = this.inventory.getItem(i);
            if (inventoryStack.is(shooterAmmunition)) {
                return inventoryStack;
            }
        }

        return this.isCreative()
            ? this.level().itematic$createStack(ItemIds.ARROW)
            : ItemStack.EMPTY;
    }

    @Unique
    @SuppressWarnings("SameParameterValue")
    private boolean isEquipped(ResourceKey<Item> item) {
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            ItemStack equippedStack = this.getItemBySlot(slot);
            if (!equippedStack.itematic$isOf(item)) {
                continue;
            }

            Equippable equippable = equippedStack.get(DataComponents.EQUIPPABLE);
            if (equippable != null && equippable.slot() == slot) {
                return true;
            }
        }

        return false;
    }
}
