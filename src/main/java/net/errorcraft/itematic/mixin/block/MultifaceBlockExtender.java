package net.errorcraft.itematic.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.MultifaceBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MultifaceBlock.class)
public class MultifaceBlockExtender extends Block {
    public MultifaceBlockExtender(Settings settings) {
        super(settings);
    }

    @Redirect(
        method = "canReplace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(this.itematic$asItemKey());
    }
}
