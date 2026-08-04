package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Leashable.class)
public interface LeashableExtender {
    @Redirect(
        method = {
            "restoreLeashFromSave",
            "dropLeash(Lnet/minecraft/world/entity/Entity;ZZ)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private static ItemEntity dropItemForLeadUseRegistryKey(Entity instance, ServerLevel world, ItemLike item) {
        return instance.itematic$dropItem(world, ItemKeys.LEAD);
    }
}
