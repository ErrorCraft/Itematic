package errorcraft.itematic.mixin.block;

import errorcraft.itematic.item.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SlabBlock.class)
public class SlabBlockExtender extends Block {
    public SlabBlockExtender(Settings settings) {
        super(settings);
    }

    @Redirect(
        method = "canReplace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfUseItemKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemUtil.keyFromBlock(this));
    }
}
