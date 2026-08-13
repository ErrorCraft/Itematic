package net.errorcraft.itematic.mixin.entity.decoration;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Painting.class)
public abstract class PaintingEntityExtender extends HangingEntity {
    protected PaintingEntityExtender(EntityType<? extends HangingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "dropItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/decoration/painting/Painting;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity dropItemForPaintingUseRegistryKey(Painting instance, ServerLevel world, ItemLike itemConvertible) {
        return this.itematic$dropItem(world, ItemIds.PAINTING);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForPaintingUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.PAINTING);
    }
}
