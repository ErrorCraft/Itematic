package net.errorcraft.itematic.mixin.world.entity.monster.illager;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Chargeable;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
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

@Mixin(Pillager.class)
public abstract class PillagerExtender extends MobExtender {
    protected PillagerExtender(EntityType<? extends AbstractIllager> type, Level level) {
        super(type, level);
    }

    @WrapMethod(
        method = "canUseNonMeleeWeapon"
    )
    private boolean useDataComponent(ItemStack item, Operation<Boolean> original) {
        return item.get(ItematicDataComponents.SHOOTER_METHOD) == Chargeable.INSTANCE;
    }

    @WrapOperation(
        method = "wantsItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isWhiteBannerCheckId(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.is(BlockItemIds.BANNER.white().item());
    }

    @WrapOperation(
        method = "getArmPose",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/illager/Pillager;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCrossbowUseDataComponent(Pillager instance, Item item, Operation<Boolean> original) {
        return instance.isHolding(stack -> stack.get(ItematicDataComponents.SHOOTER_METHOD) == Chargeable.INSTANCE);
    }

    @WrapOperation(
        method = {
            "populateDefaultEquipmentSlots",
            "applyRaidBuffs"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForCrossbowUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.CROSSBOW);
    }

    @WrapOperation(
        method = "enchantSpawnedWeapon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isCrossbowUseDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.get(ItematicDataComponents.SHOOTER_METHOD) == Chargeable.INSTANCE;
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.PILLAGER_SPAWN_EGG;
    }
}
