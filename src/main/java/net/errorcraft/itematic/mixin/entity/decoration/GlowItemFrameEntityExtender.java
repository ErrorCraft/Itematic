package net.errorcraft.itematic.mixin.entity.decoration;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GlowItemFrame.class)
public class GlowItemFrameEntityExtender extends ItemFrame {
    public GlowItemFrameEntityExtender(EntityType<? extends ItemFrame> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getFrameItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForGlowItemFrameUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.GLOW_ITEM_FRAME);
    }
}
