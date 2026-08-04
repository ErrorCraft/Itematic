package net.errorcraft.itematic.mixin.client.render.entity.state;

import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemClusterRenderState.class)
public class ItemStackEntityRenderStateExtender {
    @Redirect(
        method = "getSeedForItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getId(Lnet/minecraft/world/item/Item;)I"
        )
    )
    private static int getRawIdUseRegistryEntry(Item item, ItemStack stack) {
        return stack.getItemHolder().itematic$rawId();
    }
}
