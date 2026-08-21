package net.errorcraft.itematic.mixin.world.entity.monster.skeleton;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Skeleton.class)
public abstract class SkeletonExtender extends MobExtender {
    protected SkeletonExtender(EntityType<? extends AbstractSkeleton> type, Level level) {
        super(type, level);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.SKELETON_SPAWN_EGG;
    }
}
