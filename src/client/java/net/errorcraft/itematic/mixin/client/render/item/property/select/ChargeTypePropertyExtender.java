package net.errorcraft.itematic.mixin.client.render.item.property.select;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.item.property.select.ChargeTypeProperty;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChargeTypeProperty.class)
public class ChargeTypePropertyExtender {
    @Redirect(
        method = "getValue(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/LivingEntity;ILnet/minecraft/item/ItemDisplayContext;)Lnet/minecraft/item/CrossbowItem$ChargeType;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/ChargedProjectilesComponent;contains(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean containsForFireworkRocketUseRegistryKeyCheck(ChargedProjectilesComponent instance, Item item) {
        return instance.itematic$contains(ItemKeys.FIREWORK_ROCKET);
    }
}
