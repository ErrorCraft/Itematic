package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Silverfish.class)
public abstract class SilverfishEntityExtender extends MobEntityExtender {
    protected SilverfishEntityExtender(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.SILVERFISH_SPAWN_EGG;
    }
}
