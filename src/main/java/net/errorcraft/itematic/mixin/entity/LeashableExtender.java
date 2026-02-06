package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Leashable;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
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
            target = "Lnet/minecraft/entity/Entity;dropItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private static ItemEntity dropItemForLeadUseRegistryKey(Entity instance, ServerWorld world, ItemConvertible item) {
        return instance.itematic$dropItem(world, ItemKeys.LEAD);
    }
}
