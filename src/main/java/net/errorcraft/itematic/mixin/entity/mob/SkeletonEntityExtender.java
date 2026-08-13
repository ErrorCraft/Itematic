package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Skeleton.class)
public abstract class SkeletonEntityExtender extends MobEntityExtender {
    protected SkeletonEntityExtender(EntityType<? extends AbstractSkeleton> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.SKELETON_SPAWN_EGG;
    }
}
