package net.errorcraft.itematic.mixin.entity.decoration;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GlowItemFrameEntity.class)
public class GlowItemFrameEntityExtender extends ItemFrameEntity {
    public GlowItemFrameEntityExtender(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getAsItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForGlowItemFrameUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GLOW_ITEM_FRAME);
    }
}
