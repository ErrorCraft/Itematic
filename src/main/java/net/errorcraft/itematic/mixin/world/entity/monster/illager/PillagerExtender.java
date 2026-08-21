package net.errorcraft.itematic.mixin.world.entity.monster.illager;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Pillager.class)
public abstract class PillagerExtender extends MobExtender {
    protected PillagerExtender(EntityType<? extends AbstractIllager> type, Level level) {
        super(type, level);
    }

    @WrapMethod(
        method = "canUseNonMeleeWeapon"
    )
    private boolean useItemBehaviorComponent(ItemStack stack, Operation<Boolean> original) {
        return stack.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .map(shooter -> shooter.usesMethod(ShooterMethodType.CHARGEABLE))
            .orElse(false);
    }

    @Redirect(
        method = "wantsItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isWhiteBannerCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.WHITE_BANNER);
    }

    @Redirect(
        method = "getArmPose",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/illager/Pillager;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCrossbowCheckId(Pillager instance, Item item) {
        return instance.itematic$isHolding(ItemIds.CROSSBOW);
    }

    @Redirect(
        method = {
            "populateDefaultEquipmentSlots",
            "applyRaidBuffs"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForCrossbowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.CROSSBOW);
    }

    @Redirect(
        method = "enchantSpawnedWeapon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isCrossbowCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.CROSSBOW);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.PILLAGER_SPAWN_EGG;
    }
}
