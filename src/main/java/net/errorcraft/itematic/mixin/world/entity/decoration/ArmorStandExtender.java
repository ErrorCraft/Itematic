package net.errorcraft.itematic.mixin.world.entity.decoration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorStand.class)
public abstract class ArmorStandExtender extends LivingEntity {
    protected ArmorStandExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = {
            "brokenByPlayer",
            "getPickResult"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForArmorStandUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.ARMOR_STAND);
    }

    @WrapOperation(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isNameTagCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.NAME_TAG);
    }
}
