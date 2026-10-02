package net.errorcraft.itematic.mixin.world.entity.decoration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Painting.class)
public abstract class PaintingExtender extends HangingEntity {
    protected PaintingExtender(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = {
            "dropItem",
            "getPickResult"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPaintingUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.PAINTING);
    }
}
