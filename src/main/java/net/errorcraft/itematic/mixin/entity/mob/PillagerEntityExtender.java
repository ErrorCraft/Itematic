package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Pillager.class)
public abstract class PillagerEntityExtender extends MobEntityExtender {
    protected PillagerEntityExtender(EntityType<? extends AbstractIllager> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyReturnValue(
        method = "canUseNonMeleeWeapon",
        at = @At("TAIL")
    )
    private boolean useItemBehaviorComponent(boolean original, ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.SHOOTER)
            .map(shooter -> shooter.usesMethod(ShooterMethodTypes.CHARGEABLE))
            .orElse(false);
    }

    @Redirect(
        method = "wantsItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForWhiteBannerUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WHITE_BANNER);
    }

    @Redirect(
        method = "getArmPose",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/illager/Pillager;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingForCrossbowUseRegistryKeyCheck(Pillager instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.CROSSBOW);
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
        return this.level().itematic$createStack(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "enchantSpawnedWeapon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForCrossbowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CROSSBOW);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemKeys.PILLAGER_SPAWN_EGG;
    }
}
