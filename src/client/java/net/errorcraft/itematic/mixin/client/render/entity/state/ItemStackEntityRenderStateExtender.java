package net.errorcraft.itematic.mixin.client.render.entity.state;

import net.minecraft.client.render.entity.state.ItemStackEntityRenderState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStackEntityRenderState.class)
public class ItemStackEntityRenderStateExtender {
    @Redirect(
        method = "getSeed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getRawId(Lnet/minecraft/item/Item;)I"
        )
    )
    private static int getRawIdUseRegistryEntry(Item item, ItemStack stack) {
        return stack.getRegistryEntry().itematic$rawId();
    }
}
