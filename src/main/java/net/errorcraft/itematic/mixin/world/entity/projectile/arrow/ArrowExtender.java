package net.errorcraft.itematic.mixin.world.entity.projectile.arrow;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Arrow.class)
public abstract class ArrowExtender extends AbstractArrow {
    protected ArrowExtender(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = {
            "getDefaultPickupItem",
            "tick"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForArrowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.ARROW);
    }
}
