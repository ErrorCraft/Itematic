package net.errorcraft.itematic.mixin.entity.player;

import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityExtender extends LivingEntity implements LivingEntityAccess {
    @Shadow
    @Final
    private PlayerInventory inventory;

    @Shadow
    @Final
    private PlayerAbilities abilities;

    protected PlayerEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "updateTurtleHelmet",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForTurtleHelmetUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.TURTLE_HELMET);
    }

    @Redirect(
        method = "checkFallFlying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForElytraUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ELYTRA);
    }

    @Redirect(
        method = "damageShield",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForShieldUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SHIELD);
    }

    @Redirect(
        method = "damageShield",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> Stat<Item> getOrCreateStatForActiveItemUseRegistryEntry(StatType<Item> instance, T key) {
        return instance.itematic$getOrCreateStat(this.activeItemStack.getRegistryEntry());
    }

    @ModifyArg(
        method = "disableShield",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"
        )
    )
    private Item setCooldownForShieldUseDynamicRegistry(Item item) {
        return this.getWorld().itematic$getItem(ItemKeys.SHIELD).value();
    }

    @Redirect(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;postHit(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
        )
    )
    private void neverSetEmptyStack(PlayerEntity instance, Hand hand, ItemStack stack) {}

    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<T> doNotGetOrCreateStat(StatType<T> instance, T key) {
        return null;
    }

    @Redirect(
        method = "isUsingSpyglass",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.ZOOM);
    }

    @Override
    public ItemStack itematic$getAmmunition(ShooterItemComponent itemComponent) {
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

        return this.abilities.creativeMode ? this.getWorld().itematic$createStack(ItemKeys.ARROW) : ItemStack.EMPTY;
    }
}
