package net.errorcraft.itematic.mixin.world.entity.decoration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Cushion.class)
public abstract class CushionExtender extends BlockAttachedEntity {
    @Shadow
    public abstract DyeColor getColor();

    protected CushionExtender(EntityType<? extends BlockAttachedEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForCushionUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.CUSHION.pick(this.getColor()));
    }
}
