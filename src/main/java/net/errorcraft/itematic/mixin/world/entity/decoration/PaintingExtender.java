package net.errorcraft.itematic.mixin.world.entity.decoration;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Painting.class)
public abstract class PaintingExtender extends HangingEntity {
    protected PaintingExtender(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "dropItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/decoration/painting/Painting;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    @Nullable
    private ItemEntity spawnPaintingUseId(Painting instance, ServerLevel level, ItemLike item) {
        return this.itematic$spawnAtLocation(level, ItemIds.PAINTING);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPaintingUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.PAINTING);
    }
}
