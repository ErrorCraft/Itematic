package net.errorcraft.itematic.mixin.client.renderer.item.properties.select;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.renderer.item.properties.select.Charge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Charge.class)
public class ChargeExtender {
    @Redirect(
        method = "get(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;ILnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/world/item/CrossbowItem$ChargeType;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/ChargedProjectiles;contains(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean containsFireworkRocketCheckId(ChargedProjectiles instance, Item item) {
        return instance.itematic$contains(ItemIds.FIREWORK_ROCKET);
    }
}
