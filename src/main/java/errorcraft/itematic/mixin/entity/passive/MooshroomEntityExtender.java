package errorcraft.itematic.mixin.entity.passive;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MooshroomEntity.class)
public class MooshroomEntityExtender {
    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean interactMobIsOfUseRegistryEntryCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.BOWL);
    }
}
