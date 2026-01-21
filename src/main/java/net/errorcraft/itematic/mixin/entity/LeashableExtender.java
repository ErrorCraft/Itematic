package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Leashable;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Leashable.class)
public interface LeashableExtender {
    @Redirect(
        method = {
            "resolveLeashData",
            "detachLeash(Lnet/minecraft/entity/Entity;ZZ)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private static ItemEntity dropItemForLeadUseRegistryKey(Entity instance, ItemConvertible item) {
        return instance.itematic$dropItem(ItemKeys.LEAD);
    }
}
