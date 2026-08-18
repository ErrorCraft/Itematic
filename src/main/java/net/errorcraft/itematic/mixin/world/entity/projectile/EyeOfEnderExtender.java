package net.errorcraft.itematic.mixin.world.entity.projectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EyeOfEnder.class)
public abstract class EyeOfEnderExtender extends Entity {
    public EyeOfEnderExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getDefaultItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForEnderEyeUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.ENDER_EYE);
    }
}
