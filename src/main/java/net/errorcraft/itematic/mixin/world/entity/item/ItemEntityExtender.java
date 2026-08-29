package net.errorcraft.itematic.mixin.world.entity.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public abstract class ItemEntityExtender {
    @WrapOperation(
        method = "playerTouch",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    @Nullable
    private <T> Stat<Item> getStatCheckInteractableStackUseHolder(StatType<Item> instance, T argument, Operation<Stat<T>> original, @Local(name = "itemStack") ItemStack itemStack) {
        if (itemStack.itematic$cannotBeInteractedWith()) {
            return null;
        }

        return instance.itematic$get(itemStack.typeHolder());
    }
}
