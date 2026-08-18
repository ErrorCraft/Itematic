package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.access.world.level.block.state.BlockBehaviourAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SegmentableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SegmentableBlock.class)
public interface SegmentableBlockExtender extends BlockBehaviourAccess {
    @Redirect(
        method = "canBeReplaced",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isItemCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(this.itematic$asItemId());
    }
}
