package errorcraft.itematic.mixin.entity.mob;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PillagerEntity.class)
public class PillagerEntityExtender {
    @Redirect(
        method = "isRaidCaptain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isRaidCaptainIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.WHITE_BANNER);
    }
}
