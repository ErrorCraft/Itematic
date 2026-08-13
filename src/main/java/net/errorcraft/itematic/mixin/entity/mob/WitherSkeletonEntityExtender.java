package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitherSkeleton.class)
public abstract class WitherSkeletonEntityExtender extends MobEntityExtender {
    protected WitherSkeletonEntityExtender(EntityType<? extends AbstractSkeleton> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForStoneSwordUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.STONE_SWORD);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.WITHER_SKELETON_SPAWN_EGG;
    }
}
