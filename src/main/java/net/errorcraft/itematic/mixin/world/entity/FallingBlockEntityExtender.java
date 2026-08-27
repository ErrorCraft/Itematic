package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityExtender extends Entity {
    public FallingBlockEntityExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    @Nullable
    private ItemEntity spawnItemUseId(FallingBlockEntity instance, ServerLevel level, ItemLike item, @Local(name = "block") Block block) {
        return this.itematic$spawnAtLocation(level, block.itematic$asItemId());
    }
}
