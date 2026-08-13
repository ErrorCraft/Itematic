package net.errorcraft.itematic.mixin.entity.boss;

import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitherBoss.class)
public abstract class WitherEntityExtender extends MobEntityExtender {
    protected WitherEntityExtender(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity dropItemForNetherStarUseRegistryKey(WitherBoss instance, ServerLevel world, ItemLike itemConvertible) {
        return this.itematic$dropItem(world, ItemIds.NETHER_STAR);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.WITHER_SPAWN_EGG;
    }
}
